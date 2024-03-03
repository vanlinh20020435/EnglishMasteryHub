import { $axios } from "."

const getStudents = async (token = null) => {
    const path = '/api/students'
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

const searchStudents = async (token = null, params) => {
    const path = '/api/students/search'
    var result = {
        success: false,
        data: null
    }
    try {
        const response = await $axios.get(path, {
            params: {
                username: params.username || '',
                name: params.name || '',
                email: params.email || ''
            }, headers: { 'Authorization': 'Bearer ' + token }
        })
        result.data = response.data;
        result.success = true
    } catch (error) {
        console.log(error);
    }
    return result
}

const createAdmin = async (token = null, payload = null) => {
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

const editAdmin = async (id = null, token = null, payload = null) => {
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

export { getStudents, searchStudents, createAdmin, editAdmin }