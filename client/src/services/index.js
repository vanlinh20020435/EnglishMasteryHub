import axios from 'axios';
export { getAdmins, searchAdmins, createAdmin, editAdmin, editAdminStatus, changeAdminPassword } from './admin';
export { getTeachers, searchTeachers, createTeacher, editTeacher, editTeacherStatus, changeTeacherPassword } from './teacher';
export { getStudents, searchStudents, createStudent, editStudent, editStudentStatus, changeStudentPassword } from './student';
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
