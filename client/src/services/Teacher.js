import { $axios } from "."

function getTeachers() {
    const path = '/api/user/userinfo'
    $axios.get(path, { headers: { 'Authentication': '' } })
}

export { getTeachers }