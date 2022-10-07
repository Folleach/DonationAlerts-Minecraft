import React from 'react';
import { Client } from 'socketflow.client';
import logo from './logo.svg';
import './App.css';

var client = new Client("wss://localhost:7196/api/ws");

function App() {

  client.send(1, {"X": "hahahha"});
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.tsx</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
    </div>
  );
}

export default App;
