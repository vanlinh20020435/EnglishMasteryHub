
import axios from 'axios'
import { getAdmins } from './admin'
import { login } from './auth'

const $axios = {
    ...axios.create({
        baseURL: import.meta.env.VITE_API
    })
}

export { $axios, getAdmins, login }