import axios from "axios";

const api = axios.create({
  baseURL: "https://localhost:8080/api",
  timeout: 10000,
});

api.interceptors.request.use(function (config) { 
  return config;
}, function (error) { 
  console.log("error request:", error)
  return Promise.reject(error);
});

api.interceptors.response.use(function (response) {
  return response;
}, function (error) {
  console.log("error response:", error)
  return Promise.reject(error);
});

export default api;