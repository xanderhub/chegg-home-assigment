import React, { Component } from "react";
import QuestionsList from "./QuestionsList";

class QfetcherApp extends Component {
  render() {
    return (
      <div className="landing">
        <div className="text-dark">
          <div className="col-md-12">
            <h1>Q-Fetcher Application</h1>
            <QuestionsList />
          </div>
        </div>
      </div>
    );
  }
}

export default QfetcherApp;
