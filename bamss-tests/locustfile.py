from random import randrange;
from locust import HttpLocust, TaskSet, task, between

AUTH_URL = 'https://bamss-auth.herokuapp.com/user'
HEALTH_URL = 'https://bamss.herokuapp.com/health'
REDIRECT_URL = 'https://bamss.herokuapp.com/exo'
SHORTEN_URL = 'https://bamss.herokuapp.com/shorten'

class UserBehavior(TaskSet):
    def signup(self):
        self.username = 'LocustUser' + str(randrange(1000000, 9999999))
        self.email = self.username + '@example.com'
        self.password = self.username
        payload = {
            'username': self.username,
            'email': self.email,
            'password': self.password,
            'account_type': 'standard'
        }
        self.client.put(AUTH_URL, json=payload)
        
    @task(1)
    def login(self):
        payload = {'username': self.username, 'password': self.password}
        response = self.client.post(AUTH_URL, json=payload)
        json_response_dict = response.json()
        self.token = json_response_dict['token']

    @task(1)
    def health(self):
        self.client.get(HEALTH_URL)

    @task(8)
    def redirect(self):
        self.client.get(REDIRECT_URL)

    @task(2)
    def shroten(self):
        payload = {'token': self.token, 'original_url': 'http://www.example.com'}
        self.client.post(SHORTEN_URL, json=payload)

    def on_start(self):
        self.signup()
        self.login()
    
    username = ''
    email = ''
    password = ''
    token = ''

class WebsiteUser(HttpLocust):
    task_set = UserBehavior
    wait_time = between(5.0, 9.0)
    host = '/'
    