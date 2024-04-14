export default function datetime(timeString) {
    const instance = {}
    let [dateString, clockString] = timeString.split(' ')
    let [day, year, month] = dateString.split('/')
    let [hours, minutes] = clockString.split(':')
    instance.value = new Date(year, month, day, hours, minutes)
    instance.string = timeString
    return instance
}