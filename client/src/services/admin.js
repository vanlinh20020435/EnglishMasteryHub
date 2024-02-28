import { $axios } from "."

function getAdmins() {
    const path = '/api/user/userinfo'
    $axios.get(path, { headers: { 'Authentication': '' } })
}

export { getAdmins }