export default function datetime(timeString) {
  const instance = {};
  let [dateString, clockString] = timeString.split(' ');
  let [day, month, year] = dateString.split('/');
  let [hours, minutes] = clockString.split(':');
  instance.value = new Date(year, month - 1, day, hours, minutes);
  instance.string = timeString;
  return instance;
}
