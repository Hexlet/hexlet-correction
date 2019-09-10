import React from 'react';
import './App.css';
import {Nav, Navbar} from "react-bootstrap";

function App() {
    return (
        <div>
            <Navbar bg="light" variant="light" className="shadow-sm">
                <Navbar.Brand href="#home">Hexlet Corrections</Navbar.Brand>
                <Navbar.Toggle/>
                <Navbar.Collapse className="justify-content-end">
                    <Nav>
                        <Nav.Link href="#signin">Sign in</Nav.Link>
                        <Nav.Link href="#signup">Sign up</Nav.Link>
                    </Nav>
                </Navbar.Collapse>
            </Navbar>
            <div className="container">
                <h1>Hexlet Corrections Content</h1>
            </div>
        </div>
    );
}

export default App;
