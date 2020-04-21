import React, { Fragment } from 'react';
import Card from 'react-bootstrap/Card';
import CardDeck from 'react-bootstrap/CardDeck';
import Button from 'react-bootstrap/Button';
import ListGroup from 'react-bootstrap/ListGroup';
import ListGroupItem from  'react-bootstrap/ListGroupItem';
import {TiTick}  from 'react-icons/ti';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';

function PricingPage () {
    return (<Fragment>
        <h1>Pricing Plan </h1>
        <CardDeck style={{color: "black", width: "80%", fontSize: "80%"}}>
            <Card>
                <Card.Header>FREE</Card.Header>
                <Card.Body>
                    <ListGroup className="list-group-flush">
                        <ListGroupItem>
                            <Row>
                                <Col>API Access</Col>
                                <Col> - </Col>
                            </Row>
                        </ListGroupItem>

                        <ListGroupItem>
                            <Row>
                                <Col>Daily Shortens</Col>
                                <Col>15 URLs</Col>
                            </Row>
                        </ListGroupItem>

                        <ListGroupItem>
                            <Row>
                                <Col>Max URL Expiration</Col>
                                <Col>2 months</Col>
                            </Row>
                        </ListGroupItem>

                        <ListGroupItem>
                            <Row>
                                <Col>Monthly Price</Col>
                                <Col>$0 </Col>
                            </Row>
                        </ListGroupItem>
                    </ListGroup>
                    <br></br>

                </Card.Body>
            </Card>
            <Card>
                <Card.Header>STANDARD</Card.Header>
                <Card.Body>
                    <ListGroup className="list-group-flush">
                        <ListGroupItem>
                            <Row>
                                <Col>API Access</Col>
                                <Col> - </Col>
                            </Row>
                        </ListGroupItem>

                        <ListGroupItem>
                            <Row>
                                <Col>Daily Shortens</Col>
                                <Col>100 URLs</Col>
                            </Row>
                        </ListGroupItem>

                        <ListGroupItem>
                            <Row>
                                <Col>Max URL Expiration</Col>
                                <Col>6 months</Col>
                            </Row>
                        </ListGroupItem>
                        <ListGroupItem>
                            <Row>
                                <Col>Monthly Price</Col>
                                <Col>$7 </Col>
                            </Row>
                        </ListGroupItem>
                    </ListGroup>
                    <br></br>
                    <Button variant="primary">Purchase</Button>
                </Card.Body>
            </Card>
        </CardDeck>
        <br/>
        <CardDeck style={{color: "black", width: "80%", fontSize: "80%"}}>
            <Card>
                <Card.Header>BUSINESS</Card.Header>
                <Card.Body>
                    <ListGroup className="list-group-flush">
                        <ListGroupItem>
                            <Row>
                                <Col>API Access</Col>
                                <Col><TiTick /></Col>
                            </Row>
                        </ListGroupItem>
                        <ListGroupItem>
                            <Row>
                                <Col>Daily Shortens</Col>
                                <Col>1000 URLs</Col>
                            </Row>
                        </ListGroupItem>
                        <ListGroupItem>
                            <Row>
                                <Col>Max URL Expiration</Col>
                                <Col>12 months</Col>
                            </Row>
                        </ListGroupItem>
                        <ListGroupItem>
                            <Row>
                                <Col>Monthly Price</Col>
                                <Col>$25 </Col>
                            </Row>
                        </ListGroupItem>
                    </ListGroup>
                    <br></br>
                    <Button variant="primary">Purchase</Button>
                </Card.Body>
            </Card>
            <Card>
                <Card.Header>ADVANCED</Card.Header>
                <Card.Body>
                    <ListGroup className="list-group-flush">
                        <ListGroupItem>
                            <Row>
                                <Col>API Access</Col>
                                <Col><TiTick /></Col>
                            </Row>
                        </ListGroupItem>
                        <ListGroupItem>
                            <Row>
                                <Col>Daily Shortens</Col>
                                <Col> unlimited </Col>
                            </Row>
                        </ListGroupItem>
                        <ListGroupItem>
                            <Row>
                                <Col>Max URL Expiration</Col>
                                <Col>24 months</Col>
                            </Row>
                        </ListGroupItem>
                        <ListGroupItem>
                            <Row>
                                <Col>Monthly Price</Col>
                                <Col>$0.01/URL<br/><i>Min $50</i></Col>
                            </Row>
                        </ListGroupItem>
                    </ListGroup>
                    <br></br>
                    <Button variant="primary">Purchase</Button>
                </Card.Body>
            </Card>
        </CardDeck>
        <br/>

    </Fragment>);
}

export default PricingPage;
