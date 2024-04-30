<template>
  <v-card style="margin-top: 16px">
    <v-toolbar color="#ebebeba3" flat>
      <v-toolbar-title>Danh sách học sinh</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn icon="mdi-filter" style="margin-right: 8px" :color="isOpenFilter ? '#00bd7e' : ''"
        @click="() => (isOpenFilter = !isOpenFilter)"></v-btn>
      <v-btn color="#00bd7e" dark variant="outlined" @click="isOpenForm = true">
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
    <v-data-table :loading="isLoadingData" :headers="headers" :items="data"
      :sort-by="[{ key: 'studentId', order: 'asc' }]">
      <template v-slot:item.avatar="{ item }">
        <v-avatar>
          <v-img alt="Avatar" :src="item.avatar ||
            'https://static.thenounproject.com/png/4035887-200.png'
            "></v-img>
        </v-avatar>
      </template>
      <template v-slot:item.class="{ item }">
        {{ item.class?.className }}
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
          {{ isEdit ? `Chỉnh sửa ${formItem.name}` : 'Tạo mới' }}
        </v-card-title>
        <v-card-text>
          <v-row>
            <v-col cols="12" md="12" sm="6">
              <v-text-field v-model="formItem.name" :rules="nameRules" label="Name*"></v-text-field>
            </v-col>
            <v-col cols="12" md="12" sm="6">
              <v-text-field v-model="formItem.username" :rules="usernameRules" label="Username*"></v-text-field>
            </v-col>
            <v-col v-if="!isEdit" cols="12" md="12" sm="6">
              <v-text-field v-model="formItem.password" :rules="passwordRules" label="Password*"></v-text-field>
            </v-col>
            <v-col v-if="formItem.avatar" cols="12" md="12">
              <v-text-field prepend-icon="mdi-paperclip" v-model="formItem.avatar" label="Avatar" clearable
                readonly></v-text-field>
            </v-col>
            <v-col v-else cols="12" md="12">
              <v-file-input v-model="selectedFile" label="Avatar" accept="image/png, image/jpeg" hide-no-data
                show-size></v-file-input>
            </v-col>
            <v-col cols="12" md="12" sm="6">
              <v-text-field v-model="formItem.email" :rules="emailRules" label="Email*"></v-text-field>
            </v-col>
            <v-col cols="12" md="12" sm="6">
              <v-select label="Class*" v-model="formItem.classId" :rules="classRules" :items="classSelector"></v-select>
            </v-col>
            <v-col cols="12" md="6" sm="6">
              <v-select label="Gender*" v-model="formItem.gender" :rules="genderRules"
                :items="genderSelector"></v-select>
            </v-col>
            <v-col cols="12" md="6" sm="6">
              <v-dialog ref="dialog" v-model="isOpenDatePicker" :return-value.sync="datePicker" persistent
                width="290px">
                <template v-slot:activator="{ attrs }">
                  <v-text-field v-model="datePickerComputed" label="Birthday" readonly v-bind="attrs" clearable
                    @click="() => (isOpenDatePicker = true)"></v-text-field>
                </template>
                <v-date-picker v-model="datePicker" scrollable @update:model-value="() => (isOpenDatePicker = false)">
                </v-date-picker>
              </v-dialog>
            </v-col>
          </v-row>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn variant="tonal" @click="() => (isOpenForm = false)">
            Cancel
          </v-btn>
          <v-btn color="success" variant="flat" type="submit" :disabled="isLoadingFile">
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
              <v-text-field type="password" v-model="passUpdating.password" :rules="passwordRules"
                label="Password"></v-text-field>
            </v-col>
            <v-col cols="12" md="12" sm="6">
              <v-text-field type="password" v-model="passUpdating.repeat" :rules="repeatRules"
                label="Repeat password"></v-text-field>
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
  getStudents,
  searchStudents,
  createStudent,
  editStudent,
  editStudentStatus,
  changeStudentPassword,
  deleteStudent,
  getClasses,
  uploadFile
} from '@/services';
import { authenticationRole, toastStore } from '@/stores';
import { mapState } from 'pinia';
export default {
  components: {
    PopUpYesNo,
  },
  data() {
    return {
      isOpenChangePassword: false,
      formPasswordValid: false,
      passUpdating: {},
      itemUpdatingLock: {},
      isOpenLock: false,
      datePicker: null,
      isOpenDatePicker: false,
      formValid: false,
      headers: [
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
        { title: 'Class', key: 'class', sortable: true },
        { title: 'Status', key: 'status', sortable: false },
        { title: 'Actions', key: 'actions', sortable: false },
      ],
      genderSelector: [
        { value: 1, title: 'Male' },
        { value: 0, title: 'Female' },
      ],
      classSelector: [],
      isOpenForm: false,
      isOpenDelete: false,
      isEdit: false,
      formItem: {},
      delettingItem: {},
      data: [],
      valid: false,
      genderRules: [
        (value) => {
          if (value || value === 0) return true;
          return 'Gender is required.';
        },
      ],
      classRules: [
        (value) => {
          if (value || value === 0) return true;
          return 'Class is required.';
        },
      ],
      passwordRules: [
        (value) => {
          if (value || value === 0) return true;
          return 'Password is required.';
        },
      ],
      usernameRules: [
        (value) => {
          if (value || value === 0) return true;
          return 'Username is required.';
        },
      ],
      nameRules: [
        (value) => {
          if (value || value === 0) return true;
          return 'Name is required.';
        },
      ],
      repeatRules: [
        (value) => {
          if (value || value === 0) return true;
          return 'Password is required.';
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
      isLoadingData: false,
      isLoadingForm: false,
      datePickerComputed: null,
      filter: {
        username: '',
        name: '',
        email: '',
      },
      _timerId: null,
      isOpenFilter: false,
      selectedFile: null,
      isLoadingFile: false
    };
  },
  computed: {
    ...mapState(authenticationRole, ['authentication']),
    ...mapState(toastStore, ['updateToast']),
  },
  mounted() {
    this.fetchData();
  },
  methods: {
    async fetchData() {
      this.isLoadingData = true;
      const res = await getStudents(this.authentication?.accessToken?.token);
      if (res.success) {
        this.data = res.data;
      }
      this.isLoadingData = false;
    },
    fetchFilter() {
      clearTimeout(this._timerId);
      this._timerId = setTimeout(async () => {
        this.isLoadingData = true;
        const res = await searchStudents(
          this.authentication?.accessToken?.token,
          this.filter
        );
        if (res.success) {
          this.data = res.data;
        }
        this.isLoadingData = false;
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
        this.updateToast('success', `${updateStatus ? 'Mở khóa' : 'Khóa'} tài khoản thành công!`)
        await this.fetchData();
      } else {
        this.updateToast('error', `${updateStatus ? 'Mở khóa' : 'Khóa'} tài khoản thất bại!`)
      }
      this.isOpenLock = false
      this.itemUpdatingLock = {}
    },
    async submitChangePassword() {
      if (this.formPasswordValid) {
        const res = await changeStudentPassword(this.passUpdating?.id, this.authentication?.accessToken?.token, this.passUpdating?.password);
        if (res.success) {
          this.updateToast('success', "Đổi mật khẩu thành công!")
          await this.fetchData();
        } else {
          this.updateToast('error', "Đổi mật khẩu thất bại!")
        }
        this.isOpenChangePassword = false;
        this.passUpdating = {}
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
      const payload = {
        username: this.formItem.username,
        email: this.formItem.email,
        name: this.formItem.name,
        classId: this.formItem.classId,
        password: this.formItem.password,
        gender: this.formItem.gender,
      }
      if (this.formItem.avatar) payload.avatar = this.formItem.avatar
      if (this.formItem.birthday) payload.birthday = this.formItem.birthday
      const res = await createStudent(
        this.authentication?.accessToken?.token,
        payload
      );
      this.isLoadingForm = false;
      if (res.success) {
        this.updateToast('success', "Tạo Student thành công!")
        this.isOpenForm = false;
        await this.fetchData();
      } else {
        this.updateToast('error', "Tạo Student thất bại!")
      }
    },
    async editItem() {
      this.isLoadingForm = true;
      const payload = {
        username: this.formItem.username,
        email: this.formItem.email,
        name: this.formItem.name,
        classId: this.formItem.classId,
        password: this.formItem.password,
        gender: this.formItem.gender,
      };
      if (this.formItem.avatar) payload.avatar = this.formItem.avatar
      if (this.formItem.birthday) payload.birthday = this.formItem.birthday
      const res = await editStudent(
        this.formItem.studentId,
        this.authentication?.accessToken?.token,
        payload
      );
      this.isLoadingForm = false;
      if (res.success) {
        this.updateToast('success', "Sửa Student thành công!")
        this.isOpenForm = false;
        await this.fetchData();
      } else {
        this.updateToast('error', "Sửa Student thất bại!")
      }
    },
    async deleteItem() {
      this.isLoadingForm = true;
      const res = await deleteStudent(
        this.authentication?.accessToken?.token,
        this.delettingItem.studentId
      );
      if (res.success) {
        this.updateToast('success', "Xóa Student thành công!")
        await this.fetchData();
      } else {
        this.updateToast('error', "Xóa Student thất bại!")
      }
      this.isOpenDelete = false
      this.isLoadingForm = false;
    },
    openEdit(item) {
      this.isOpenForm = true;
      this.formItem = { ...item };
      this.formItem.classId = this.formItem.class.classId
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
        this.datePickerComputed = null
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
    datePickerComputed(val) {
      this.formItem.birthday = val;
    },
    async selectedFile(val) {
      if (val) {
        try {
          this.isLoadingFile = true
          const result = await uploadFile(
            this.authentication?.accessToken?.token,
            val[0]
          )
          if (result.success) {
            this.formItem.avatar = result.data.url
          } else {
            this.selectedFile = null
            this.updateToast('error', "Lỗi tải ảnh lên!")
          }
        } catch (error) {
          this.selectedFile = null
          console.log(error);
        }
        this.isLoadingFile = false
      }
    },
    "formItem.avatar": function (val) {
      if (!val) {
        this.selectedFile = null
      }
    }
  },
};
</script>
