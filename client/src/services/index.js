
import axios from 'axios'
import { getAdmins, createAdmin, editAdmin } from './admin'
import { login, getUserInfo } from './auth'

const $axios = {
    ...axios.create({
        baseURL: import.meta.env.VITE_API
    })
}

export { $axios, getAdmins, createAdmin, editAdmin, login, getUserInfo }