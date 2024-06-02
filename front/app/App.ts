import axios, { AxiosError } from "axios";

export const BASE_URL = "http://localhost:8080"
// export const BASE_URL = "http://200.132.38.218:8004"
const api = axios.create({
  baseURL: BASE_URL,
  timeout: 10000,
});

api.interceptors.request.use(function (config) {
  return config;
}, function (error: AxiosError) {
  console.log("error request:", error);
  return Promise.reject(error);
});

api.interceptors.response.use(function (response) {
  return response;
}, 
function (error) { 
  return Promise.reject(error);
});

export default api;