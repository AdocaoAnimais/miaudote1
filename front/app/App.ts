import axios, { AxiosError } from "axios";

// export const BASE_URL = "http://localhost:8080"
export const BASE_URL = "http://200.132.38.218:8004"
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
function (error: AxiosError) { 
  const response: { data: Problem } = error.response as { data: Problem } 
  const problem = response?.data ?? response
  return Promise.reject(problem);
});

export default api;


export class Problem{

  title: string | null = null;
  detail: string | null = null;
  status: number | null = null;
  type: string | null = null;
  extra: any | null = null;

  constructor(title: string | null, detail: string | null, status: number | null, type: string | null, extra: any | null, ){
    this.title = title;
    this.detail = detail;
    this.status = status;
    this.type = type;
    this.extra = extra;
  }
}