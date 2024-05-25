import axios, { AxiosError } from "axios";

const api = axios.create({
  baseURL: "https://localhost:8080/api",
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
  if (error.response?.status === 401) {
    console.log(error.response)
    window.location.href = '/login';
  }
  return Promise.reject(error);
});

export default api;