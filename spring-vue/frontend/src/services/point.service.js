import axios from 'axios';
import authHeader from './auth-header';

const API_URL = 'http://localhost:8080/api/';

class PointService {
  getAll() {
    return axios.get(API_URL + 'points', { headers: authHeader() });
  }

  create(data) {
    return axios.post(API_URL + 'points', data,{ headers: authHeader() });
  }

  deleteAll() {
    return axios.delete(API_URL + 'points',{ headers: authHeader() });
  }
}

export default new PointService();
