import axios from "axios";

const SERVER = "http://localhost:8080";
const QUESTION_API_URL = SERVER.concat("/api");

class QuestionDataService {
  retrieveQuestions(page, size, source) {
    return axios.get(
      `${QUESTION_API_URL}/questions?${
        source === undefined ? "" : "source=".concat(source)
      }&page=${page}&size=${size}`
    );
  }

  getNextPage(nextPageUrl) {
    return axios.get(`${SERVER}${nextPageUrl}`);
  }
}

export default new QuestionDataService();
