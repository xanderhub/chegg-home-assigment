import React, { Component } from "react";
import QuestionDataService from "../service/QuestionDataService";
import Select from "react-select";
import "bootstrap/dist/css/bootstrap.min.css";

const PAGE_SIZE = 3;
const START_PAGE = 0;

const sources = [
  { label: "JSON", value: 1 },
  { label: "CSV", value: 2 },
  { label: "IMAGE", value: 3 }
];

class QuestionsList extends Component {
  constructor(props) {
    super(props);
    this.state = {
      questions: [],
      source: undefined,
      page: START_PAGE,
      nextPageUrl: undefined
    };
    this.refreshQuestions = this.refreshQuestions.bind(this);
    this.getNextQuestions = this.getNextQuestions.bind(this);
  }

  componentDidMount() {
    this.refreshQuestions();
  }

  refreshQuestions(page = START_PAGE, pageSize = PAGE_SIZE, source) {
    QuestionDataService.retrieveQuestions(page, pageSize, source).then(
      response => {
        this.setState({
          questions: response.data.questions,
          nextPageUrl: response.data.pagination.next
        });
      }
    );
  }

  getNextQuestions(nextPageUrl) {
    QuestionDataService.getNextPage(nextPageUrl).then(response => {
      this.setState({
        questions: response.data.questions,
        nextPageUrl: response.data.pagination.next
      });
    });
  }

  render() {
    return (
      <div className="container">
        <div className="col-md-3">
          <h5>
            Filter by source:
            <Select
              options={sources}
              onChange={e => {
                this.setState({
                  source: e.label,
                  page: START_PAGE
                });
                this.refreshQuestions(START_PAGE, PAGE_SIZE, e.label);
              }}
            />
          </h5>
        </div>
        <div className="container">
          <table className="table">
            <thead>
              <tr>
                <th>Source</th>
                <th>Question</th>
              </tr>
            </thead>
            <tbody>
              {this.state.questions.map(q => (
                <tr>
                  <td>{q.source}</td>
                  <td>{q.value}</td>
                </tr>
              ))}
            </tbody>
          </table>
          <div className="row">
            <button
              className="btn btn-success"
              onClick={() => {
                this.getNextQuestions(this.state.nextPageUrl);
              }}
              disabled={this.state.nextPageUrl === null ? true : false}
            >
              Next
            </button>
          </div>
        </div>
      </div>
    );
  }
}

export default QuestionsList;
