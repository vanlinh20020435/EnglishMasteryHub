<template>
  <v-card style="margin-top: 16px">
    <v-toolbar color="#ebebeba3" flat>
      <v-toolbar-title>Danh sách học sinh</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn icon="mdi-filter" style="margin-right: 8px" :color="isOpenFilter ? '#00bd7e' : ''"
        @click="() => (isOpenFilter = !isOpenFilter)"></v-btn>
      <v-btn class="mb-2" color="#00bd7e" dark variant="outlined" @click="isOpenForm = true">
        Tạo mới
      </v-btn>
    </v-toolbar>
    <v-row v-if="isOpenFilter" style="padding: 8px; margin-top: 8px">
      <v-col cols="12" md="4">
        <v-text-field v-model="filter.name" label="Name" @update:model-value="fetchFilter" clearable></v-text-field>
      </v-col>
      <v-col cols="12" md="4">
        <v-text-field v-model="filter.username" label="Username" @update:model-value="fetchFilter"
          clearable></v-text-field>
      </v-col>
      <v-col cols="12" md="4">
        <v-text-field v-model="filter.email" label="Email" @update:model-value="fetchFilter" clearable></v-text-field>
      </v-col>
    </v-row>
    <v-data-table :loading="isLoadingStudent" :headers="studentsHeaders" :items="students"
      :sort-by="[{ key: 'studentId', order: 'asc' }]">
      <template v-slot:item.avatar="{ item }">
        <v-avatar>
          <v-img alt="Avatar" :src="item.avatar ||
        'https://avatars0.githubusercontent.com/u/9064066?v=4&s=460'
        "></v-img>
        </v-avatar>
      </template>

      <template v-slot:item.gender="{ item }">
        {{ item.gender ? 'Male' : 'Female' }}
      </template>

      <template v-slot:item.status="{ item }">
        <v-chip @click="() => openLock(item)" variant="elevated" :color="item.status ? 'success' : 'error'">{{
        item.status ? 'Active' : 'Inactive'
      }}</v-chip>
      </template>

      <template v-slot:item.actions="{ item }">
        <v-icon class="me-2" color="primary" size="small" @click="() => openEdit(item)">
          mdi-pencil
        </v-icon>
        <v-icon class="me-2" color="warning" @click="() => openChangePassword(item)">
          mdi-key-variant
        </v-icon>
        <v-icon size="small" color="error" @click="() => openDelete(item)">
          mdi-delete
        </v-icon>
      </template>
    </v-data-table>
  </v-card>
  <v-dialog v-model="isOpenForm" max-width="500px">
    <v-form v-model="formValid" @submit.prevent="submitForm">
      <v-card>
        <v-card-title>
          <span class="text-h5">hihi</span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row>
              <v-col cols="12" md="12" sm="6">
                <v-text-field v-model="formItem.name" :rules="requireRules" label="Name"></v-text-field>
              </v-col>
              <v-col cols="12" md="12" sm="6">
                <v-text-field v-model="formItem.username" :rules="requireRules" label="Username"></v-text-field>
              </v-col>
              <v-col v-if="!isEdit" cols="12" md="12" sm="6">
                <v-text-field v-model="formItem.password" :rules="requireRules" label="Password"></v-text-field>
              </v-col>
              <v-col cols="12" md="12" sm="6">
                <v-text-field v-model="formItem.avatar" label="Avatar"></v-text-field>
              </v-col>
              <v-col cols="12" md="12" sm="6">
                <v-text-field v-model="formItem.email" :rules="emailRules" label="Email"></v-text-field>
              </v-col>
              <v-col cols="12" md="12" sm="6">
                <v-select label="Class" v-model="formItem.classId" :items="classSelector"></v-select>
              </v-col>
              <v-col cols="12" md="6" sm="6">
                <v-select label="Gender" v-model="formItem.gender" :items="genderSelector"></v-select>
              </v-col>
              <v-col cols="12" md="6" sm="6">
                <v-dialog ref="dialog" v-model="isOpenDatePicker" :return-value.sync="datePicker" persistent
                  width="290px">

                  <template v-slot:activator="{ attrs }">
                    <v-text-field v-model="datePickerComputed" :rules="requireRules" label="Birthday" readonly
                      v-bind="attrs" clearable @click="() => (isOpenDatePicker = true)"></v-text-field>
                  </template>
                  <v-date-picker v-model="datePicker" scrollable @update:model-value="() => (isOpenDatePicker = false)">
                  </v-date-picker>
                </v-dialog>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn variant="tonal" @click="() => (isOpenForm = false)">
            Cancel
          </v-btn>
          <v-btn color="success" variant="flat" type="submit">
            Save
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-form>
  </v-dialog>
  <v-dialog v-model="isOpenChangePassword" max-width="500px">
    <v-form v-model="formPasswordValid" @submit.prevent="submitChangePassword">
      <v-card>
        <v-card-title>
          Đổi mật khẩu
        </v-card-title>
        <v-card-text>
          <v-row>
            <v-col cols="12" md="12" sm="6">
              <v-text-field v-model="passUpdating.password" :rules="requireRules" label="Password"></v-text-field>
            </v-col>
            <v-col cols="12" md="12" sm="6">
              <v-text-field v-model="passUpdating.repeat" :rules="repeatRules" label="Repeat password"></v-text-field>
            </v-col>
          </v-row>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn variant="tonal" @click="() => (isOpenChangePassword = false)">
            Cancel
          </v-btn>
          <v-btn color="success" variant="flat" type="submit">
            Save
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-form>
  </v-dialog>
  <PopUpYesNo :msg="`Bạn có chắc chắn muốn ${itemUpdatingLock.status ? 'khóa' : 'mở khóa'}?`" :visible="isOpenLock"
    :handleClickYes="updateLock" :handleClickNo="() => (isOpenLock = false)" />
  <PopUpYesNo :msg="`Bạn có chắc chắn muốn xoá học sinh ${delettingItem.name}?`" :visible="isOpenDelete"
    :handleClickYes="deleteItem" :handleClickNo="() => (isOpenDelete = false)" />
</template>

<script>
import PopUpYesNo from '@/components/popup/PopUpYesNo.vue';
import {
  getStudentsOfClass,
  searchStudents,
  createStudent,
  editStudent,
  editStudentStatus,
  changeStudentPassword,
  deleteStudent,
  getClasses
} from '@/services';
import { authenticationRole, toastStore } from '@/stores';
import { mapState } from 'pinia';
export default {
  components: {
    PopUpYesNo,
  },
  data: () => ({
    delettingItem: {},
    isOpenChangePassword: false,
    formPasswordValid: false,
    passUpdating: {},
    itemUpdatingLock: {},
    isOpenLock: false,
    isLoadingStudent: false,
    students: [],
    studentsHeaders: [
      {
        title: 'Student ID',
        align: 'center',
        key: 'studentId',
        sortable: false,
      },
      { title: 'Avatar', key: 'avatar', sortable: false },
      { title: 'Name', key: 'name', sortable: false },
      { title: 'Username', key: 'username', sortable: false },
      { title: 'Email', key: 'email', sortable: false },
      { title: 'Birthday', key: 'birthday', sortable: false },
      { title: 'Gender', key: 'gender', sortable: false },
      { title: 'Status', key: 'status', sortable: false },
      { title: 'Actions', key: 'actions', sortable: false },
    ],
    genderSelector: [
      { value: 1, title: 'Male' },
      { value: 0, title: 'Female' },
    ],
    classSelector: [],
    requireRules: [
      (value) => {
        if (value || value === 0) return true;
        return 'Name is required.';
      },
    ],
    repeatRules: [
      (value) => {
        if (value || value === 0) return true;
        return 'Name is required.';
      },
      (value) => {
        if (this.passUpdating?.password === value) return true;
        return "Haven't equal yet!";
      },
    ],
    emailRules: [
      (value) => {
        if (value) return true;
        return 'E-mail is requred.';
      },
      (value) => {
        if (/.+@.+\..+/.test(value)) return true;
        return 'E-mail must be valid.';
      },
    ],
    isOpenDatePicker: false,
    datePicker: null,
    datePickerComputed: null,
    isOpenForm: false,
    isOpenDelete: false,
    isOpenFilter: false,
    formValid: false,
    isEdit: false,
    formItem: {},
    filter: {
      username: '',
      name: '',
      email: '',
    },
    _timerId: null,
  }),
  async mounted() {
    await this.fetchStudents();
  },
  computed: {
    ...mapState(authenticationRole, ['authentication']),
    ...mapState(toastStore, ['updateToast']),
  },
  methods: {
    async fetchStudents() {
      this.isLoadingStudent = true;
      const res = await getStudentsOfClass(
        this.authentication?.accessToken?.token,
        this.$route.params.id
      );
      if (res.success) {
        this.students = res.data;
      }
      this.isLoadingStudent = false;
    },
    fetchFilter() {
      clearTimeout(this._timerId);
      this._timerId = setTimeout(async () => {
        this.isLoadingStudent = true;
        const res = await searchStudents(
          this.authentication?.accessToken?.token,
          this.filter
        );
        if (res.success) {
          this.data = res.data;
        }
        this.isLoadingStudent = false;
      }, 500);
    },
    openLock(item) {
      this.isOpenLock = true
      this.itemUpdatingLock = item
    },
    openChangePassword(item) {
      this.isOpenChangePassword = true
      this.passUpdating.id = item.studentId
    },
    async updateLock() {
      const updateStatus = this.itemUpdatingLock?.status ? 0 : 1
      const res = await editStudentStatus(this.itemUpdatingLock?.studentId, this.authentication?.accessToken?.token, updateStatus)
      if (res.success) {
        await this.fetchStudents();
      } else {
        //error
      }
      this.isOpenLock = false
      this.itemUpdatingLock = {}
    },
    async submitChangePassword() {
      if (this.formPasswordValid) {
        const res = await changeStudentPassword(this.passUpdating?.id, this.authentication?.accessToken?.token, this.passUpdating?.password);
        this.isOpenChangePassword = false;
        this.passUpdating = {}
        await this.fetchStudents();
      }
    },
    async submitForm() {
      if (this.formValid) {
        if (this.datePicker)
          this.formItem.birthday = new Date(
            this.datePicker
          ).toLocaleDateString();
        if (this.isEdit) {
          await this.editItem();
        } else {
          await this.createItem();
        }
        this.isOpenForm = false;
      }
    },
    async createItem() {
      this.isLoadingForm = true;
      const res = await createStudent(
        this.authentication?.accessToken?.token,
        this.formItem
      );
      this.isLoadingForm = false;
      if (res.success) {
        console.log(res);
        this.isOpenForm = false;
        await this.fetchStudents();
      } else {
        //error
      }
    },
    async editItem() {
      this.isLoadingForm = true;
      const payload = {
        username: this.formItem.username,
        email: this.formItem.email,
        name: this.formItem.name,
        gender: this.formItem.gender,
        avatar: this.formItem.avatar,
        birthday: this.formItem.birthday,
        classId: this.formItem.classId,
      };
      const res = await editStudent(
        this.formItem.studentId,
        this.authentication?.accessToken?.token,
        payload
      );
      this.isLoadingForm = false;
      if (res.success) {
        console.log(res);
        this.isOpenForm = false;
        await this.fetchStudents();
      } else {
        //error
      }
    },
    async deleteItem() {
      this.isLoadingForm = true;
      const res = await deleteStudent(
        this.authentication?.accessToken?.token,
        this.delettingItem.studentId
      );
      if (res.success) {
        console.log(res);
        await this.fetchData();
      } else {
        //error
      }
      this.isOpenDelete = false
      this.isLoadingForm = false;
    },
    pickerFocussing(val) {
      if (val) this.menu = true;
    },
    openEdit(item) {
      this.isOpenForm = true;
      this.formItem = { ...item };
      this.isEdit = true;
      this.datePickerComputed = this.formItem.birthday
    },
    openDelete(item) {
      this.isOpenDelete = true;
      this.delettingItem = { ...item };
    },
  },
  watch: {
    isOpenDelete(val) {
      if (!val) this.delettingItem = {};
    },
    isOpenChangePassword(val) {
      if (!val) this.passUpdating = {};
    },
    async isOpenForm(val) {
      if (!val) {
        this.formItem = {};
        this.isEdit = false;
        this.datePicker = null;
      } else {
        const res = await getClasses(this.authentication?.accessToken?.token)
        if (res.success) {
          this.classSelector = res.data.map(cls => ({ title: cls.className, value: cls.classId }))
        }
      }
    },
    datePicker(val) {
      this.datePickerComputed = val ? new Date(val).toLocaleDateString() : null;
    },
  },
};
</script>
