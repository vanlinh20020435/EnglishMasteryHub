<template>
  <v-container fluid grid-list-sm>
    <v-row>
      <v-col v-for="i in 6" :key="i" :cols="cols"
        ><img
          :src="`https://randomuser.me/api/portraits/men/${i + 20}.jpg`"
          class="image"
          alt="lorem"
          width="100%"
          height="100%" />
      </v-col>
    </v-row>
  </v-container>
  <v-footer class="mt-5"></v-footer>
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
                <v-text-field
                  v-model="formItem.name"
                  :rules="requireRules"
                  label="Name"></v-text-field>
              </v-col>
              <v-col cols="12" md="12" sm="6">
                <v-text-field
                  v-model="formItem.username"
                  :rules="requireRules"
                  label="Username"></v-text-field>
              </v-col>
              <v-col v-if="!isEdit" cols="12" md="12" sm="6">
                <v-text-field
                  v-model="formItem.password"
                  :rules="requireRules"
                  label="Password"></v-text-field>
              </v-col>
              <v-col cols="12" md="12" sm="6">
                <v-text-field
                  v-model="formItem.avatar"
                  label="Avatar"></v-text-field>
              </v-col>
              <v-col cols="12" md="12" sm="6">
                <v-text-field
                  v-model="formItem.email"
                  :rules="emailRules"
                  label="Email"></v-text-field>
              </v-col>
              <v-col cols="12" md="6" sm="6">
                <v-text-field
                  v-model="formItem.gender"
                  :rules="requireRules"
                  label="Gender"></v-text-field>
              </v-col>
              <v-col cols="12" md="6" sm="6">
                <v-dialog
                  ref="dialog"
                  v-model="isOpenDatePicker"
                  :return-value.sync="datePicker"
                  persistent
                  width="290px">
                  <template v-slot:activator="{ attrs }">
                    <v-text-field
                      v-model="datePickerComputed"
                      :rules="requireRules"
                      label="Birthday"
                      readonly
                      v-bind="attrs"
                      clearable
                      @click="() => (isOpenDatePicker = true)"></v-text-field>
                  </template>
                  <v-date-picker
                    v-model="datePicker"
                    scrollable
                    @update:model-value="() => (isOpenDatePicker = false)">
                  </v-date-picker>
                </v-dialog>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="blue-darken-1"
            variant="text"
            @click="() => (isOpenForm = false)">
            Cancel
          </v-btn>
          <v-btn color="blue-darken-1" variant="text" type="submit">
            Save
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-form>
  </v-dialog>
</template>

<script>
import PopUpYesNo from '@/components/popup/PopUpYesNo.vue';
import { getClasses, createAdmin, editAdmin } from '@/services';
import { authenticationRole, toastStore } from '@/stores';
import { mapState } from 'pinia';

export default {
  components: {
    PopUpYesNo,
  },
  props: {
    data: Array,
    loading: Boolean,
    reload: Function,
  },
  data() {
    return {
      datePicker: null,
      isOpenDatePicker: false,
      formValid: false,
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
      formItem: {},
      delettingItem: {},
      requireRules: [
        (value) => {
          if (value || value === 0) return true;
          return 'Name is required.';
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
      isLoadingForm: false,
      datePickerComputed: null,
    };
  },
  computed: {
    ...mapState(authenticationRole, ['authentication']),
    ...mapState(toastStore, ['updateToast']),
    cols() {
      const { lg, md, sm } = this.$vuetify.display;
      return lg ? 2 : md ? 3 : sm ? 4 : 6;
    },
  },
  methods: {
    async fetchData() {
      this.loading = true;
      const res = await getClasses(this.authentication?.accessToken?.token);
      if (res.success) {
        this.data = res.data;
      }
      this.loading = false;
    },
    submitFilter() {},
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
        await this.reload();
      }
    },
    async createItem() {
      this.isLoadingForm = true;
      const res = await createAdmin(
        this.authentication?.accessToken?.token,
        this.formItem
      );
      this.isLoadingForm = false;
      if (res.success) {
        console.log(res);
        this.isOpenForm = false;
        await this.reload();
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
      };
      const res = await editAdmin(
        this.formItem.adminId,
        this.authentication?.accessToken?.token,
        payload
      );
      this.isLoadingForm = false;
      if (res.success) {
        console.log(res);
        this.isOpenForm = false;
        await this.reload();
      } else {
        //error
      }
    },
    deleteItem() {},
    pickerFocussing(val) {
      if (val) this.menu = true;
    },
    openEdit(item) {
      this.isOpenForm = true;
      this.formItem = { ...item };
      this.datePicker = new Date(this.formItem.birthday);
      console.log(111111111111, this.datePicker);
      this.isEdit = true;
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
    isOpenForm(val) {
      if (!val) {
        this.formItem = {};
        this.isEdit = false;
        this.datePicker = null;
      }
    },
    datePicker(val) {
      this.datePickerComputed = val ? new Date(val).toLocaleDateString() : null;
    },
  },
};
</script>
