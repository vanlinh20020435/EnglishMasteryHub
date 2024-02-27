<template>
  <v-card class="height-100">
    <v-container class="d-flex flex-column height-100">
      <v-row class="align-center">
        <v-col cols="12" md="8" class="header_title font-bold color-primary">
          Quản lý bài kiểm tra
        </v-col>
        <v-col cols="12" md="4" style="padding-right: 5%">
          <v-row class="d-flex align-center">
            <v-autocomplete
              :items="items"
              auto-select-first
              class="flex-full-width"
              density="comfortable"
              item-props
              menu-icon=""
              placeholder="Tìm kiếm tên bài kiểm tra"
              prepend-inner-icon="mdi-magnify"
              rounded
              theme="light"
              variant="solo"
              hide-details
              color="#00bd7e"
            ></v-autocomplete>

            <v-icon
              color="#00bd7e"
              size="x-large"
              class="cursor-pointer"
              style="padding-left: 2rem"
            >
              mdi-plus-circle
            </v-icon>
          </v-row>
        </v-col>
      </v-row>
      <v-divider class="header_divider" :thickness="2"></v-divider>
      <v-row style="height: 90%">
        <v-col class="height-100" cols="12" md="12">
          <v-data-table
            style="overflow-y: auto"
            class="height-100 scrollbar-custom"
            fixed-header
            :headers="headers"
            :items="desserts"
            :sort-by="[{ key: 'calories', order: 'asc' }]"
          >
            <template v-slot:top>
              <!-- <v-toolbar-title>My CRUD</v-toolbar-title>
                  <v-divider class="mx-4" inset vertical></v-divider> -->
              <v-spacer></v-spacer>
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
              <v-dialog v-model="dialogDelete" max-width="500px">
                <v-card>
                  <v-card-title class="text-h5"
                    >Bạn có chắc chắn muốn xóa bài kiểm tra này?</v-card-title
                  >
                  <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn
                      color="blue-darken-1"
                      variant="text"
                      @click="closeDelete"
                      >Không</v-btn
                    >
                    <v-btn
                      color="blue-darken-1"
                      variant="text"
                      @click="deleteItemConfirm"
                      >Có</v-btn
                    >
                    <v-spacer></v-spacer>
                  </v-card-actions>
                </v-card>
              </v-dialog>
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
                mdi-delete
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
export default {
  name: "ManageExam",
  data: () => ({
    selected: [],
    dialog: false,
    dialogDelete: false,
    headers: [
      {
        title: "ID",
        align: "start",
        sortable: false,
        key: "id",
      },
      {
        title: "Tên bài kiểm tra",
        sortable: false,
        key: "name",
      },
      { title: "Danh mục", key: "calories" },
      { title: "Số câu hỏi", key: "fat" },
      { title: "Ngày tạo", key: "carbs" },
      // { title: "Protein (g)", key: "protein" },
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
  }),

  computed: {
    formTitle() {
      return this.editedIndex === -1 ? "New Item" : "Edit Item";
    },
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

  methods: {
    initialize() {
      this.desserts = [
        {
          id: 1,
          name: "Frozen Yogurt",
          calories: 159,
          fat: 6.0,
          carbs: 24,
          protein: 4.0,
        },
        {
          id: 2,
          name: "Ice cream sandwich",
          calories: 237,
          fat: 9.0,
          carbs: 37,
          protein: 4.3,
        },
        {
          id: 3,
          name: "Eclair",
          calories: 262,
          fat: 16.0,
          carbs: 23,
          protein: 6.0,
        },
        {
          id: 4,
          name: "Cupcake",
          calories: 305,
          fat: 3.7,
          carbs: 67,
          protein: 4.3,
        },
        {
          id: 5,
          name: "Gingerbread",
          calories: 356,
          fat: 16.0,
          carbs: 49,
          protein: 3.9,
        },
        {
          id: 6,
          name: "Jelly bean",
          calories: 375,
          fat: 0.0,
          carbs: 94,
          protein: 0.0,
        },
        {
          id: 7,
          name: "Lollipop",
          calories: 392,
          fat: 0.2,
          carbs: 98,
          protein: 0,
        },
        {
          id: 8,
          name: "Honeycomb",
          calories: 408,
          fat: 3.2,
          carbs: 87,
          protein: 6.5,
        },
        {
          id: 9,
          name: "Donut",
          calories: 452,
          fat: 25.0,
          carbs: 51,
          protein: 4.9,
        },
        {
          id: 10,
          name: "KitKat",
          calories: 518,
          fat: 26.0,
          carbs: 65,
          protein: 7,
        },
        {
          id: 11,
          name: "KitKat",
          calories: 518,
          fat: 26.0,
          carbs: 65,
          protein: 7,
        },
        {
          id: 12,
          name: "KitKat",
          calories: 518,
          fat: 26.0,
          carbs: 65,
          protein: 7,
        },
        {
          id: 13,
          name: "KitKat",
          calories: 518,
          fat: 26.0,
          carbs: 65,
          protein: 7,
        },
      ];
    },

    editItem(item) {
      this.editedIndex = this.desserts.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialog = true;
    },

    deleteItem(item) {
      this.editedIndex = this.desserts.indexOf(item);
      this.editedItem = Object.assign({}, item);
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
  },
};
</script>

<style>
@import "./Exam.style.scss";
</style>
