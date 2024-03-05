import axios from 'axios';
export { getAdmins, searchAdmins, createAdmin, editAdmin } from './admin';
export { getTeachers, searchTeachers, createTeacher } from './teacher';
export { getStudents, searchStudents, createStudent } from './student';
export {
  getClasses,
  getClass,
  getStudentsOfClass,
  searchClasses,
} from './class';
export { login, getUserInfo } from './auth';

const $axios = {
  ...axios.create({
    baseURL: import.meta.env.VITE_API,
  }),
};

export { $axios };
