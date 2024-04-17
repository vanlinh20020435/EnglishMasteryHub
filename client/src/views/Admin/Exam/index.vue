<template>
    <v-card class="height-100">
        <v-toolbar color="#ebebeba3" flat>
            <v-toolbar-title>Quản lý bài kiểm tra</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-btn icon="mdi-filter" style="margin-right: 8px" :color="isOpenFilter ? '#00bd7e' : ''"
                @click="() => (isOpenFilter = !isOpenFilter)"></v-btn>
            <v-btn class="mb-2" color="#00bd7e" dark variant="outlined"
                @click="() => $router.push(`/${authentication.user.role}/exam/add`)">
                Tạo mới
            </v-btn>
        </v-toolbar>
        <v-row v-if="isOpenFilter" style="padding: 8px; margin-top: 8px">
            <v-col cols="12" md="12">
                <v-text-field v-model="searchValue" label="Name" clearable></v-text-field>
            </v-col>
        </v-row>
        <v-data-table style="overflow-y: auto" fixed-header :headers="headers" :items="filteredStateExams"
            :sort-by="[{ key: 'testId', order: 'asc' }]" :loading="isLoading" hover
            @click:row="(a, b) => handleClickItem(b.item)">
            <template v-slot:item.actions="{ item }">
                <v-icon color="primary" class="me-2" @click.stop.prevent="() => editItem(item)">
                    mdi-pencil
                </v-icon>
                <v-icon color="error" @click.stop.prevent="() => handleDelete(item)">
                    mdi-delete
                </v-icon>
            </template>
            <template v-slot:no-data>
                <div class="exam_empty d-flex flex-column align-center">
                    <img src="@/assets/images/img_empty_exam.png" alt="Empty Exam" class="img_empty" />
                    <h3 class="font-bold">{{ msgEmptyExam }}</h3>
                </div>
            </template>
        </v-data-table>
        <PopUpYesNo :msg="`Bạn có chắc chắn muốn xóa bài kiểm tra ${deleteItem.testName}?`" :visible="isOnenDelete"
            :handleClickYes="deleteItemConfirm" :handleClickNo="() => isOnenDelete = false" />
    </v-card>
</template>

<script>
import PopUpYesNo from "@/components/popup/PopUpYesNo.vue";
import { mapState } from "pinia";
import { authenticationRole } from "@/stores";
import { apiCallerGet, apiCallerDelete } from "@/services/teacher";
import { removeVietnameseDiacritics } from "@/base/helper";

export default {
    components: {
        PopUpYesNo,
    },
    data: () => ({
        isLoading: false,
        isOnenDelete: false,
        headers: [
            {
                title: "Test ID",
                align: "start",
                key: "testId",
            },
            {
                title: "Tên bài kiểm tra",
                key: "testName",
                sortable: false,
            },
            { title: "Người tạo", key: "creator", sortable: false },
            {
                title: "Thời gian (phút)", key: "time",
                sortable: false,
            },
            {
                title: "Số câu hỏi", key: "totalQuestions",
                sortable: false,
            },
            {
                title: "Ngày tạo", key: "created",
                sortable: false,
            },
            { title: "Mô tả", key: "description", sortable: false },
            {
                title: "Actions",
                key: "actions",
                sortable: false,
            }
        ],
        stateExams: {
            data: [],
            loading: true,
            error: false,
            success: false,
            msg: "",
        },
        searchValue: "",
        isOpenFilter: false,
        deleteItem: {}
    }),
    props: {
        isAll: Boolean,
        isAdmin: Boolean,
    },
    computed: {
        ...mapState(authenticationRole, ["updateAuth", "authentication"]),
        msgEmptyExam() {
            return this.stateExams.data.length ? "Không tìm thấy bài kiểm tra nào!" : "Bạn chưa có bài kiểm tra nào!"
        },
        filteredStateExams() {
            if (!this.searchValue) {
                return this.stateExams.data;
            } else {
                const normalizedSearchInput = removeVietnameseDiacritics(
                    this.searchValue.toLowerCase()?.trim()
                );
                return this.stateExams.data.filter((exam) => {
                    const normalizedTitle = removeVietnameseDiacritics(
                        exam.testName.toLowerCase()?.trim()
                    );
                    if (normalizedSearchInput == "d") {
                        return (
                            normalizedTitle.includes("d") || normalizedTitle.includes("đ")
                        );
                    } else {
                        return normalizedTitle.includes(normalizedSearchInput);
                    }
                });
            }
        },
    },
    mounted() {
        this.fetchData();
    },
    methods: {
        async fetchData() {
            this.isLoading = true;
            let urlFetch = "/api/testss"
            const result = await apiCallerGet(urlFetch);
            if (result?.data) {
                this.isLoading = false;
                this.stateExams.data = result.data;
            }
        },
        editItem(item) {
            console.log("Edit: ", item);
            this.$router.push(
                `/${this.authentication.user.role}/exam/edit/${item.testId}`
            );
        },
        handleDelete(item) {
            this.isOnenDelete = true
            this.deleteItem = item
        },
        async deleteItemConfirm() {
            const result = await apiCallerDelete(
                "/api/testss/" + this.deleteItem.testId
            );
            if (result.success) {
                this.stateExams.success = true;
                this.fetchDataExam();
            }
            this.closeDelete();
            this.isOnenDelete = false
        },
        handleClickItem(item) {
            console.log("item ==", item);
        },
        handleSearchInputChange(valueSearch) {
            this.searchValue = valueSearch;
        },
    },
};
</script>