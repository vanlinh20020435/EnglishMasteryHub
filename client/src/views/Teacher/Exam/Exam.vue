<template>
  <v-card class="height-100">
    <v-container class="d-flex flex-column height-100 v-container__full">
      <HeaderTitle
        isSearch
        isCreate
        title="Quản lý bài kiểm tra"
        :createNew="createNewExam"
      />
      <v-divider class="header_divider" :thickness="2"></v-divider>
      <v-row style="height: 90%" class="d-flex justify-center">
        <v-col class="height-100" cols="12" md="11">
          <v-data-table
            style="overflow-y: auto"
            class="height-100 scrollbar-custom v-data-table__exam"
            fixed-header
            :headers="headers"
            :items="this.stateExams.data"
            :sort-by="[{ key: 'calories', order: 'asc' }]"
          >
            <template v-slot:top>
              <!-- <v-toolbar-title>My CRUD</v-toolbar-title>
                  <v-divider class="mx-4" inset vertical></v-divider> 
              <v-spacer></v-spacer>-->
              <v-dialog v-model="dialog" max-width="500px">
                <!-- <template v-slot:activator="{ props }">
                  <v-btn color="primary" dark class="mb-2" v-bind="props">
                    New Item
                  </v-btn>
                </template> -->
                <v-card>
                  <v-card-title>
                    <span class="text-h5">{{ formTitle }}</span>
                  </v-card-title>

                  <v-card-text>
                    <v-container>
                      <v-row>
                        <v-col cols="12" sm="6" md="4">
                          <v-text-field
                            v-model="editedItem.name"
                            label="Dessert name"
                          ></v-text-field>
                        </v-col>
                        <v-col cols="12" sm="6" md="4">
                          <v-text-field
                            v-model="editedItem.calories"
                            label="Calories"
                          ></v-text-field>
                        </v-col>
                        <v-col cols="12" sm="6" md="4">
                          <v-text-field
                            v-model="editedItem.fat"
                            label="Fat (g)"
                          ></v-text-field>
                        </v-col>
                        <v-col cols="12" sm="6" md="4">
                          <v-text-field
                            v-model="editedItem.carbs"
                            label="Carbs (g)"
                          ></v-text-field>
                        </v-col>
                        <v-col cols="12" sm="6" md="4">
                          <v-text-field
                            v-model="editedItem.protein"
                            label="Protein (g)"
                          ></v-text-field>
                        </v-col>
                      </v-row>
                    </v-container>
                  </v-card-text>

                  <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="blue-darken-1" variant="text" @click="close">
                      Cancel
                    </v-btn>
                    <v-btn color="blue-darken-1" variant="text" @click="save">
                      Save
                    </v-btn>
                  </v-card-actions>
                </v-card>
              </v-dialog>
              <PopUpYesNo
                msg="Bạn có chắc chắn muốn xóa bài kiểm tra này?"
                :visible="dialogDelete"
                :handleClickYes="deleteItemConfirm"
                :handleClickNo="closeDelete"
                @update:visible="handleVisible"
              />
            </template>
            <template v-slot:item.actions="{ item }">
              <v-icon
                color="#00bd7e"
                size="default"
                class="me-2"
                @click="editItem(item)"
              >
                mdi-pencil
              </v-icon>
              <v-icon color="red" size="default" @click="deleteItem(item)">
                mdi-trash-can
              </v-icon>
            </template>
            <template v-slot:no-data>
              <v-btn color="primary" @click="initialize"> Reset </v-btn>
            </template>
          </v-data-table>
        </v-col>
      </v-row>
    </v-container>
  </v-card>
</template>

<script>
import HeaderTitle from "@/components/header/HeaderTitle.vue";
import PopUpYesNo from "@/components/popup/PopUpYesNo.vue";
import { mapState } from "pinia";
import { authenticationRole } from "@/stores";
import { apiCaller } from "@/services/teacher";
import { ref } from "vue";

export default {
  name: "ManageExam",
  components: {
    HeaderTitle,
    PopUpYesNo,
  },
  data: () => ({
    selected: [],
    dialog: false,
    dialogDelete: false,
    headers: [
      {
        title: "ID",
        align: "start",
        sortable: false,
        key: "testId",
      },
      {
        title: "Tên bài kiểm tra",
        key: "testName",
      },
      { title: "Mô tả", key: "description", sortable: false },
      { title: "Số câu hỏi", key: "totalQuestions" },
      { title: "Ngày tạo", key: "created" },
      { title: "Thời gian (phút)", key: "time" },
      { title: "Thao tác", key: "actions", sortable: false },
    ],
    desserts: [],
    editedIndex: -1,
    editedItem: {
      name: "",
      calories: 0,
      fat: 0,
      carbs: 0,
      protein: 0,
    },
    defaultItem: {
      name: "",
      calories: 0,
      fat: 0,
      carbs: 0,
      protein: 0,
    },
    stateExams: ref({
      data: [],
      loading: true,
      error: null,
    }),
  }),

  computed: {
    formTitle() {
      return this.editedIndex === -1 ? "New Item" : "Edit Item";
    },
    ...mapState(authenticationRole, ["updateAuth"]),
  },

  watch: {
    dialog(val) {
      val || this.close();
    },
    dialogDelete(val) {
      val || this.closeDelete();
    },
  },

  created() {
    this.initialize();
  },
  mounted() {
    this.fetchDataExam();
  },
  methods: {
    async fetchDataExam() {
      const result = await apiCaller("/api/testss");
      this.stateExams.data = result.data;
      console.log(this.stateExams.data);
    },
    initialize() {},

    editItem(item) {
      this.editedIndex = this.desserts.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialog = true;
    },

    deleteItem(item) {
      this.editedIndex = this.desserts.indexOf(item);
      this.editedItem = Object.assign({}, item);
      console.log(this.dialogDelete);
      this.dialogDelete = true;
    },

    deleteItemConfirm() {
      this.desserts.splice(this.editedIndex, 1);
      this.closeDelete();
    },

    close() {
      this.dialog = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });
    },

    closeDelete() {
      this.dialogDelete = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });
    },

    save() {
      if (this.editedIndex > -1) {
        Object.assign(this.desserts[this.editedIndex], this.editedItem);
      } else {
        this.desserts.push(this.editedItem);
      }
      this.close();
    },
    createNewExam() {
      this.dialog = true;
    },
    handleVisible(newValue) {
      this.dialogDelete = newValue;
    },
  },
};
</script>

<style>
@import "./Exam.style.scss";
</style>
