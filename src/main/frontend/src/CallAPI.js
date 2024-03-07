import axios from "axios";

export function callAPI(method, requestName, data, xsrfToken){

    return axios({
        method: method,
        url: process.env.REACT_APP_BACKEND_API_URL+requestName,
        data: data,
        withCredentials:true,
        headers: {
                 "Content-Type": "application/json; charset=utf-8",
                         "Accept": "application/json",
                         "Authorization": localStorage.getItem("token"),
                         "X-XSRF-TOKEN": xsrfToken,
                         "Localization": localStorage.getItem("Localization") 
                   }
      })
}

export function callAPINoAuth(method, requestName, data, xsrfToken){
   
    return axios({
        method: method,
        url: process.env.REACT_APP_BACKEND_API_URL+requestName,
        data: data,
        withCredentials:true,
        headers: {
                 "Content-Type": "application/json; charset=utf-8",
                         "Accept": "application/json",
                         "X-XSRF-TOKEN": xsrfToken,
                         "Localization": localStorage.getItem("Localization")
                   }
      })
}

export function callAPIMultipartFile(method, requestName, data, xsrfToken){
    
    return axios({
        method: method,
        url: process.env.REACT_APP_BACKEND_API_URL+requestName,
        data: data,
        withCredentials:true,
        headers: {
                 "Content-Type": "multipart/form-data",
                         "Accept": "application/json",
                         "Authorization": localStorage.getItem("token"),
                         "X-XSRF-TOKEN": xsrfToken,
                         "Localization": localStorage.getItem("Localization")
                   }
      })
}
