<template>
    <v-card>
        <v-row>
            <v-col md="2">
                <img style="width: 100%; height: 100%" src="../../../assets/images/class.png"></img>
            </v-col>
            <v-col md="10">
                <div>
                    <h2>{{ classInfo?.className }}</h2>
                    <p><strong>Teacher: </strong>{{ classInfo?.teacher?.name }}</p>
                    <p><strong>Time: </strong>{{ classInfo?.startDate }} - {{ classInfo.endDate }}</p>
                    <p><strong>Description: </strong>{{ classInfo?.description }}</p>
                </div>
            </v-col>
        </v-row>
    </v-card>
    <v-tabs v-model="tab" bg-color="#00bd7e" style="margin-top: 16px;">
        <v-tab :value="0" :style="tab === 0 ? 'background: #039e6a' : ''">Danh sách học sinh</v-tab>
        <v-tab :value="1" :style="tab === 0 ? '' : 'background: #039e6a'">Danh sách bài kiểm tra</v-tab>
    </v-tabs>
    <v-card v-if="tab === 0" style="margin-top: 16px;">
        <v-toolbar color="#ebebeba3" flat>
            <v-toolbar-title>Danh sách quản trị viên</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-btn icon="mdi-magnify" style="margin-right: 8px;" :color="isOpenFilter ? '#00bd7e' : ''"
                @click="() => isOpenFilter = !isOpenFilter"></v-btn>
            <v-btn class="mb-2" color="#00bd7e" dark variant="outlined" @click="isOpenForm = true">
                Tạo mới
            </v-btn>
        </v-toolbar>
        <v-row v-if="isOpenFilter" style="padding: 8px; margin-top: 8px">
            <v-col cols="12" md="4">
                <v-text-field v-model="filter.name" label="Name" @update:model-value="fetchFilter"
                    clearable></v-text-field>
            </v-col>
            <v-col cols="12" md="4">
                <v-text-field v-model="filter.username" label="Username" @update:model-value="fetchFilter"
                    clearable></v-text-field>
            </v-col>
            <v-col cols="12" md="4">
                <v-text-field v-model="filter.email" label="Email" @update:model-value="fetchFilter"
                    clearable></v-text-field>
            </v-col>
        </v-row>
        <v-data-table :loading="isLoadingStudent" :headers="studentsHeaders" :items="students"
            :sort-by="[{ key: 'adminId', order: 'asc' }]">

            <template v-slot:item.avatar="{ item }">
                <v-avatar>
                    <v-img alt="Avatar"
                        :src="item.avatar || 'https://avatars0.githubusercontent.com/u/9064066?v=4&s=460'"></v-img>
                </v-avatar>
            </template>

            <template v-slot:item.actions="{ item }">
                <v-icon class="me-2" color="primary" size="small" @click="() => openEdit(item)">
                    mdi-pencil
                </v-icon>
                <v-icon size="small" color="error" @click="() => openDelete(item)">
                    mdi-delete
                </v-icon>
                <PopUpYesNo msg="Bạn có chắc chắn muốn xoas?" :visible="isOpenDelete" :handleClickYes="deleteItem"
                    :handleClickNo="() => isOpenDelete = false" />
            </template>
        </v-data-table>
    </v-card>
    <v-card v-else>
        hihi
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
                                    <v-date-picker v-model="datePicker" scrollable
                                        @update:model-value="() => isOpenDatePicker = false">
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
    </v-dialog>
</template>

<script>
import { getClass, getStudentsOfClass, searchStudents } from "@/services";
import { authenticationRole, toastStore } from "@/stores";
import { mapState } from "pinia";
export default {
    data: () => ({
        tab: 0,
        classInfo: {},
        isLoadingData: false,
        isLoadingStudent: false,
        students: [],
        studentsHeaders: [
            { title: 'AdminId', align: 'start', key: 'adminId', sortable: false },
            { title: 'Avatar', key: 'avatar', sortable: false },
            { title: 'Name', key: 'name', sortable: false },
            { title: 'Username', key: 'username', sortable: false },
            { title: 'Email', key: 'email', sortable: false },
            { title: 'Birthday', key: 'birthday', sortable: false },
            { title: 'Gender', key: 'gender', sortable: false },
            { title: 'Status', key: 'status', sortable: false },
            { title: 'Actions', key: 'actions', sortable: false },
        ],
        isOpenFilter: false,
        filter: {
            username: '',
            name: '',
            email: ''
        },
        _timerId: null,
    }),
    async mounted() {
        await this.fetchData();
        await this.fetchStudents();
    },
    computed: {
        ...mapState(authenticationRole, ["authentication"]),
        ...mapState(toastStore, ["updateToast"])
    },
    methods: {
        async fetchData() {
            this.isLoadingData = true
            const res = await getClass(this.authentication?.accessToken?.token, this.$route.params.id)
            if (res.success) {
                this.classInfo = res.data
            }
            this.isLoadingData = false
        },
        async fetchStudents() {
            this.isLoadingStudent = true
            const res = await getStudentsOfClass(this.authentication?.accessToken?.token, this.$route.params.id)
            if (res.success) {
                this.students = res.data
            }
            this.isLoadingStudent = false
        },
        fetchFilter() {
            clearTimeout(this._timerId)
            this._timerId = setTimeout(async () => {
                this.isLoadingData = true
                const res = await searchStudents(this.authentication?.accessToken?.token, this.filter)
                if (res.success) {
                    this.data = res.data
                }
                this.isLoadingData = false
            }, 500)

        },
    },
};
</script>
