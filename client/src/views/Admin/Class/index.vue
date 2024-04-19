<template>
  <v-card style="margin-top: 16px">
    <v-toolbar color="#ebebeba3" flat>
      <v-toolbar-title>Danh sách lớp học</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn icon="mdi-filter" style="margin-right: 8px" :color="isOpenFilter ? '#00bd7e' : ''"
        @click="() => (isOpenFilter = !isOpenFilter)"></v-btn>
      <div style="display: flex; margin-right: 8px">
        <v-btn :color="listType ? '' : '#00bd7e'" icon="mdi-view-grid" @click="() => (listType = 0)" />
        <v-divider vertical />
        <v-btn :color="listType ? '#00bd7e' : ''" icon="mdi-menu" @click="() => (listType = 1)" />
      </div>
      <v-btn color="#00bd7e" dark variant="outlined" @click="() => isOpenForm = true">
        Tạo mới
      </v-btn>
    </v-toolbar>
    <v-row v-if="isOpenFilter" style="padding: 8px; margin-top: 8px">
      <v-col cols="12" md="6">
        <v-text-field v-model="filter.className" label="Class name" @update:model-value="fetchFilter"
          clearable></v-text-field>
      </v-col>
      <v-col cols="12" md="6">
        <v-text-field v-model="filter.teacherId" label="Teacher ID" @update:model-value="fetchFilter"
          clearable></v-text-field>
      </v-col>
    </v-row>
    <ListClasses v-if="listType" :data="data" :loading="isLoadingData" :reload="fetchData"
      :emitEdit="(item) => openEdit(item)" :emitDelete="(item) => openDelete(item)" />
    <GridClasses v-else :data="data" :loading="isLoadingData" :reload="fetchData" />
  </v-card>
  <v-dialog v-model="isOpenForm" max-width="500px">
    <v-form v-model="formValid" @submit.prevent="submitForm">
      <v-card>
        <v-card-title>
          {{ isEdit ? `Chỉnh sửa ${formItem.className}` : 'Tạo mới' }}
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row>
              <v-col cols="12" md="12" sm="6">
                <v-text-field v-model="formItem.className" :rules="classNameRules" label="Class name*"></v-text-field>
              </v-col>
              <v-col cols="12" md="12" sm="6">
                <v-text-field v-model="formItem.avatar" label="Avatar"></v-text-field>
              </v-col>
              <v-col v-if="!isEdit" cols="12" md="12" sm="6">
                <v-text-field v-model="formItem.description" label="Description"></v-text-field>
              </v-col>
              <v-col cols="12" md="6" sm="6">
                <v-dialog ref="dialog" v-model="isOpenDatePicker1" :return-value.sync="datePicker1" persistent
                  width="290px">
                  <template v-slot:activator="{ attrs }">
                    <v-text-field v-model="datePickerComputed1" label="Start" readonly v-bind="attrs" clearable
                      @click="() => isOpenDatePicker1 = true"></v-text-field>
                  </template>
                  <v-date-picker v-model="datePicker1" scrollable @update:model-value="() => isOpenDatePicker1 = false">
                  </v-date-picker>
                </v-dialog>
              </v-col>
              <v-col cols="12" md="6" sm="6">
                <v-dialog ref="dialog" v-model="isOpenDatePicker2" :return-value.sync="datePicker2" persistent
                  width="290px">
                  <template v-slot:activator="{ attrs }">
                    <v-text-field v-model="datePickerComputed2" label="End" readonly v-bind="attrs" clearable
                      @click="() => isOpenDatePicker2 = true"></v-text-field>
                  </template>
                  <v-date-picker v-model="datePicker2" scrollable @update:model-value="() => isOpenDatePicker2 = false">
                  </v-date-picker>
                </v-dialog>
              </v-col>
              <v-col cols="12" md="12" sm="6">
                <v-select label="Teacher*" v-model="formItem.teacherId" :rules="teacherRules"
                  :items="teacherSelector"></v-select>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue-darken-1" variant="text" @click="() => isOpenForm = false">
            Cancel
          </v-btn>
          <v-btn color="blue-darken-1" variant="text" type="submit">
            Save
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-form>
  </v-dialog>
  <PopUpYesNo msg="Bạn có chắc chắn muốn xoas?" :visible="isOpenDelete" :handleClickYes="deleteItem"
    :handleClickNo="() => (isOpenDelete = false)" />
</template>

<script>
import { getClasses, searchClasses, getTeachers, createClass, editClass, deleteClass } from '@/services';
import { authenticationRole, toastStore } from '@/stores';
import { mapState } from 'pinia';
import ListClasses from './ListClasses.vue';
import GridClasses from './GridClasses.vue';
import PopUpYesNo from '@/components/popup/PopUpYesNo.vue';

export default {
  components: {
    ListClasses,
    GridClasses,
    PopUpYesNo,
  },
  data() {
    return {
      headers: [
        { title: 'className', align: 'start', key: 'adminId', sortable: false },
        { title: 'startDate', key: 'avatar', sortable: false },
        { title: 'endDate', key: 'name', sortable: false },
        { title: 'teacherId', key: 'username', sortable: false },
        { title: 'description', key: 'email', sortable: false },
        { title: 'Actions', key: 'actions', sortable: false },
      ],
      isOpenForm: false,
      isOpenDelete: false,
      isEdit: false,
      formValid: false,
      datePicker1: null,
      isOpenDatePicker1: false,
      datePickerComputed1: null,
      datePicker2: null,
      isOpenDatePicker2: false,
      datePickerComputed2: null,
      formItem: {},
      delettingItem: {},
      data: [],
      isLoadingData: false,
      valid: false,
      listType: 1,
      isOpenFilter: false,
      filter: {
        className: '',
        teacherId: '',
      },
      _timerId: null,
      classNameRules: [
        value => {
          if (value || value === 0) return true
          return 'Class name is required.'
        },
      ],
      teacherRules: [
        value => {
          if (value || value === 0) return true
          return 'Teacher is required.'
        },
      ],
      isLoadingForm: false,
      teacherSelector: [],
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
      const res = await getClasses(this.authentication?.accessToken?.token);
      if (res.success) {
        this.data = res.data;
      }
      this.isLoadingData = false;
    },
    fetchFilter() {
      clearTimeout(this._timerId);
      this._timerId = setTimeout(async () => {
        this.isLoadingData = true;
        const res = await searchClasses(
          this.authentication?.accessToken?.token,
          this.filter
        );
        if (res.success) {
          this.data = res.data;
        }
        this.isLoadingData = false;
      }, 500);
    },
    async submitForm() {
      if (this.formValid) {
        if (this.datePicker1) this.formItem.startDate = new Date(this.datePicker1).toLocaleDateString();
        if (this.datePicker2) this.formItem.endDate = new Date(this.datePicker2).toLocaleDateString();
        if (this.isEdit) {
          await this.editItem()
        } else {
          await this.createItem()
        }
        this.isOpenForm = false
      }
    },
    async createItem() {
      this.isLoadingForm = true
      const payload = {
        className: this.formItem.className,
        teacherId: this.formItem.teacherId,
      }
      if (this.formItem.avatar) payload.avatar = this.formItem.avatar
      if (this.formItem.description) payload.description = this.formItem.description
      if (this.formItem.startDate) payload.startDate = this.formItem.startDate
      if (this.formItem.endDate) payload.endDate = this.formItem.endDate
      const res = await createClass(this.authentication?.accessToken?.token, payload)
      this.isLoadingForm = false
      if (res.success) {
        this.updateToast('success', "Tạo Student thành công!")
        this.isOpenForm = false
        await this.fetchData()
      } else {
        this.updateToast('error', "Tạo Student thất bại!")
      }
    },
    async editItem() {
      this.isLoadingForm = true
      const payload = {
        className: this.formItem.className,
        teacherId: this.formItem.teacherId,
      }
      if (this.formItem.avatar) payload.avatar = this.formItem.avatar
      if (this.formItem.description) payload.description = this.formItem.description
      if (this.formItem.startDate) payload.startDate = this.formItem.startDate
      if (this.formItem.endDate) payload.endDate = this.formItem.endDate
      const res = await editClass(this.authentication?.accessToken?.token, this.formItem.classId, payload)
      this.isLoadingForm = false
      if (res.success) {
        this.updateToast('success', "Sửa Student thành công!")
        this.isOpenForm = false
        await this.fetchData()
      } else {
        this.updateToast('error', "Sửa Student thất bại!")
      }
    },
    async deleteItem() {
      this.isLoadingForm = true;
      const res = await deleteClass(
        this.authentication?.accessToken?.token,
        this.delettingItem.classId
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
      this.isOpenForm = true
      this.formItem = { ...item }
      this.formItem.teacherId = this.formItem.teacher.teacherId
      this.isEdit = true
      this.datePickerComputed1 = this.formItem.startDate
      this.datePickerComputed2 = this.formItem.endDate
    },
    openDelete(item) {
      this.isOpenDelete = true
      this.delettingItem = { ...item }
    }
  },
  watch: {
    isOpenDelete(val) {
      if (!val) this.delettingItem = {}
    },
    async isOpenForm(val) {
      if (!val) {
        this.formItem = {}
        this.isEdit = false
        this.datePicker1 = null
        this.datePicker2 = null
        this.datePickerComputed1 = null
        this.datePickerComputed2 = null
      } else {
        const res = await getTeachers(this.authentication?.accessToken?.token)
        if (res.success) {
          this.teacherSelector = res.data.map(cls => ({ title: cls.name, value: cls.teacherId }))
        }
      }
    },
    datePicker1(val) {
      this.datePickerComputed1 = val ? new Date(val).toLocaleDateString() : null
    },
    datePicker2(val) {
      this.datePickerComputed2 = val ? new Date(val).toLocaleDateString() : null
    }
  },
};
</script>
