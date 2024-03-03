<template>
    <v-card style="margin-top: 16px;">
        <v-toolbar color="#ebebeba3" flat>
            <v-toolbar-title>Danh sách quản trị viên</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-btn icon="mdi-magnify" style="margin-right: 8px;" :color="isOpenFilter ? '#00bd7e' : ''"
                @click="() => isOpenFilter = !isOpenFilter"></v-btn>

            <!-- <div style="display: flex; margin-right: 8px;">
                <v-btn :color="listType ? '' : '#00bd7e'" icon="mdi-view-grid" @click="() => listType = 0" />
                <v-divider vertical />
                <v-btn :color="listType ? '#00bd7e' : ''" icon="mdi-menu" @click="() => listType = 1" />
            </div> -->
            <v-btn class="mb-2" color="#00bd7e" dark variant="outlined" @click="isOpenForm = true">
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
        <ListClasses v-if="listType" :data="data" :loading="isLoadingData" :reload="fetchData" />
        <GridClasses v-else :data="data" :loading="isLoadingData" :reload="fetchData" />
    </v-card>
    <!-- <v-dialog v-model="isOpenForm" max-width="500px">
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
                                <v-text-field v-model="formItem.username" :rules="requireRules"
                                    label="Username"></v-text-field>
                            </v-col>
                            <v-col v-if="!isEdit" cols="12" md="12" sm="6">
                                <v-text-field v-model="formItem.password" :rules="requireRules"
                                    label="Password"></v-text-field>
                            </v-col>
                            <v-col cols="12" md="12" sm="6">
                                <v-text-field v-model="formItem.avatar" label="Avatar"></v-text-field>
                            </v-col>
                            <v-col cols="12" md="12" sm="6">
                                <v-text-field v-model="formItem.email" :rules="emailRules" label="Email"></v-text-field>
                            </v-col>
                            <v-col cols="12" md="6" sm="6">
                                <v-text-field v-model="formItem.gender" :rules="requireRules"
                                    label="Gender"></v-text-field>
                            </v-col>
                            <v-col cols="12" md="6" sm="6">
                                <v-dialog ref="dialog" v-model="isOpenDatePicker" :return-value.sync="datePicker"
                                    persistent width="290px">
                                    <template v-slot:activator="{ attrs }">
                                        <v-text-field v-model="datePickerComputed" :rules="requireRules"
                                            label="Picker in dialog" readonly v-bind="attrs" clearable
                                            @click="() => isOpenDatePicker = true"></v-text-field>
                                    </template>
<v-date-picker v-model="datePicker" scrollable @update:model-value="() => isOpenDatePicker = false">
</v-date-picker>
</v-dialog>
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
</v-dialog> -->
</template>

<script>
import { getClasses, searchClasses, editAdmin } from '@/services'
import { authenticationRole, toastStore } from "@/stores";
import { mapState } from "pinia";
import ListClasses from "./ListClasses.vue";
import GridClasses from "./GridClasses.vue";

export default {
    components: {
        ListClasses,
        GridClasses
    },
    data() {
        return {
            // datePicker: null,
            // isOpenDatePicker: false,
            // formValid: false,
            // headers: [
            //     { title: 'className', align: 'start', key: 'adminId', sortable: false },
            //     { title: 'startDate', key: 'avatar', sortable: false },
            //     { title: 'endDate', key: 'name', sortable: false },
            //     { title: 'teacherId', key: 'username', sortable: false },
            //     { title: 'description', key: 'email', sortable: false },
            //     { title: 'Actions', key: 'actions', sortable: false },
            // ],
            // isOpenForm: false,
            // isOpenDelete: false,
            // isEdit: false,
            // formItem: {},
            // delettingItem: {},
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
            // requireRules: [
            //     value => {
            //         if (value || value === 0) return true
            //         return 'Name is required.'
            //     },
            // ],
            // emailRules: [
            //     value => {
            //         if (value) return true
            //         return 'E-mail is requred.'
            //     },
            //     value => {
            //         if (/.+@.+\..+/.test(value)) return true
            //         return 'E-mail must be valid.'
            //     },
            // ],
            // isLoadingForm: false,
            // datePickerComputed: null
        }
    },
    computed: {
        ...mapState(authenticationRole, ["authentication"]),
        ...mapState(toastStore, ["updateToast"])
    },
    mounted() {
        this.fetchData()
    },
    methods: {
        async fetchData() {
            this.isLoadingData = true
            const res = await getClasses(this.authentication?.accessToken?.token)
            if (res.success) {
                this.data = res.data
            }
            this.isLoadingData = false
        },
        fetchFilter() {
            clearTimeout(this._timerId)
            this._timerId = setTimeout(async () => {
                this.isLoadingData = true
                const res = await searchClasses(this.authentication?.accessToken?.token, this.filter)
                if (res.success) {
                    this.data = res.data
                }
                this.isLoadingData = false
            }, 500)

        },
        submitFilter() {
        },
        //     async submitForm() {
        //         if (this.formValid) {
        //             if (this.datePicker) this.formItem.birthday = new Date(this.datePicker).toLocaleDateString();
        //             if (this.isEdit) {
        //                 await this.editItem()
        //             } else {
        //                 await this.createItem()
        //             }
        //             this.isOpenForm = false
        //             await this.fetchData()
        //         }
        //     },
        //     async createItem() {
        //         this.isLoadingForm = true
        //         const res = await createAdmin(this.authentication?.accessToken?.token, this.formItem)
        //         this.isLoadingForm = false
        //         if (res.success) {
        //             console.log(res);
        //             this.isOpenForm = false
        //             await this.fetchData()
        //         } else {
        //             //error
        //         }
        //     },
        //     async editItem() {
        //         this.isLoadingForm = true
        //         const payload = {
        //             username: this.formItem.username,
        //             email: this.formItem.email,
        //             name: this.formItem.name,
        //             gender: this.formItem.gender,
        //             avatar: this.formItem.avatar,
        //             birthday: this.formItem.birthday
        //         }
        //         const res = await editAdmin(this.formItem.adminId, this.authentication?.accessToken?.token, payload)
        //         this.isLoadingForm = false
        //         if (res.success) {
        //             console.log(res);
        //             this.isOpenForm = false
        //             await this.fetchData()
        //         } else {
        //             //error
        //         }
        //     },
        //     deleteItem() {

        //     },
        //     pickerFocussing(val) {
        //         if (val) this.menu = true
        //     },
        //     openEdit(item) {
        //         this.isOpenForm = true
        //         this.formItem = { ...item }
        //         this.isEdit = true
        //     },
        //     openDelete(item) {
        //         this.isOpenDelete = true
        //         this.delettingItem = { ...item }
        //     }
        // },
        // watch: {
        //     isOpenDelete(val) {
        //         if (!val) this.delettingItem = {}
        //     },
        //     isOpenForm(val) {
        //         if (!val) {
        //             this.formItem = {}
        //             this.isEdit = false
        //             this.datePicker = null
        //         }
        //     },
        //     datePicker(val) {
        //         this.datePickerComputed = val ? new Date(val).toLocaleDateString() : null
        //     }
    }
}
</script>