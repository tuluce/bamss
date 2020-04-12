import React from 'react';
import '../../style/App.css';
import {ListGroup,Button, Row, Container, Col} from 'react-bootstrap';

function ProfilePage() {
  return (
    <div className="App">
      <header className="App-header">
        <Container>
          <Row xs={2} md={4} lg={6}>
            <Col>Username: </Col>
            <Col>Email : </Col>
          </Row>
        </Container>
        <ListGroup >
          <ListGroup.Item>URL1
            <Button href="#">Analytics</Button>
          </ListGroup.Item>
          <ListGroup.Item>URL2
            <Button href="#">Analytics</Button>
          </ListGroup.Item>
          <ListGroup.Item>URL3
            <Button href="#">Analytics</Button>
          </ListGroup.Item>
          <ListGroup.Item>URL4
            <Button href="#">Analytics</Button>
          </ListGroup.Item>
          <ListGroup.Item>URL5
            <Button href="#">Analytics</Button>
          </ListGroup.Item>
        </ListGroup>
      </header>
    </div>
  );
}

export default ProfilePage;
