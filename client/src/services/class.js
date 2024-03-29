import { $axios } from "."

export const getClasses = async (token = null) => {
    const path = '/api/class'
    var result = {
        success: false,
        data: null
    }
    try {
        const response = await $axios.get(path, { headers: { 'Authorization': 'Bearer ' + token } })
        result.data = response.data;
        result.success = true
    } catch (error) {
        console.log(error);
    }
    return result
}

export const getClass = async (token = null, id = null) => {
    const path = `/api/class/${id}`
    var result = {
        success: false,
        data: null
    }
    try {
        const response = await $axios.get(path, { headers: { 'Authorization': 'Bearer ' + token } })
        result.data = response.data;
        result.success = true
    } catch (error) {
        console.log(error);
    }
    return result
}

export const getStudentsOfClass = async (token = null, id = null) => {
    const path = `/api/class/${id}/students/get-all`
    var result = {
        success: false,
        data: null
    }
    try {
        const response = await $axios.get(path, { headers: { 'Authorization': 'Bearer ' + token } })
        result.data = response.data;
        result.success = true
    } catch (error) {
        console.log(error);
    }
    return result
}

export const getTestsOfClass = async (token = null, id = null) => {
    const path = `/api/class/${id}/tests/get-all`
    var result = {
        success: false,
        data: null
    }
    try {
        const response = await $axios.get(path, { headers: { 'Authorization': 'Bearer ' + token } })
        result.data = response.data;
        result.success = true
    } catch (error) {
        console.log(error);
    }
    return result
}

export const searchClasses = async (token = null, params) => {
    const path = '/api/class/search'
    var result = {
        success: false,
        data: null
    }
    const healthyParams = {}
    if (params.className) healthyParams.className = params.className
    if (params.teacherId) healthyParams.teacherId = params.teacherId
    try {
        const response = await $axios.get(path
            , { headers: { 'Authorization': 'Bearer ' + token }, params: healthyParams })
        result.data = response.data;
        result.success = true
    } catch (error) {
        console.log(error);
    }
    return result
}

export const createAdmin = async (token = null, payload = null) => {
    const path = '/api/admins'
    var result = {
        success: false,
        data: null
    }
    try {
        const response = await $axios.post(path, payload, { headers: { 'Authorization': 'Bearer ' + token } })
        result.data = response.data;
        result.success = true
    } catch (error) {
        console.log(error);
    }
    return result
}

export const editAdmin = async (id = null, token = null, payload = null) => {
    const path = '/api/admins'
    var result = {
        success: false,
        data: null
    }
    try {
        const response = await $axios.put(path + `/${id}`, payload, { headers: { 'Authorization': 'Bearer ' + token } })
        result.data = response.data;
        result.success = true
    } catch (error) {
        console.log(error);
    }
    return result
}