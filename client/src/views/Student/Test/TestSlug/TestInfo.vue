<template>
  <v-card v-if="isLoading" class="d-flex justify-center">
    <v-progress-circular :size="70" :width="7" color="success" indeterminate></v-progress-circular>
  </v-card>
  <div v-else-if="test.testId">
    <v-card>
      <div style="display: flex">
        <div style="flex: 1">
          <v-card-title style="font-size: 1.75rem">{{
            test.testName
          }}</v-card-title>
          <v-card-subtitle class="d-flex align-center" style="padding-bottom: 4px">
            <v-icon>mdi-clock-outline</v-icon>{{ test.time }} phút
            <v-icon style="margin-left: 16px">mdi-text-box-edit-outline</v-icon>{{ test.totalQuestions }} câu
          </v-card-subtitle>
        </div>
        <div>
          <v-card-text style="
              flex: 0;
              padding: 8px 0 4px;
              min-width: 230px;
              font-size: 1rem;
            ">
            Thời gian bắt đầu: {{ test.startDate }}
          </v-card-text>
          <v-card-text style="
              flex: 0;
              padding: 0px 0 4px;
              min-width: 230px;
              font-size: 1rem;
            ">
            Thời gian kết thúc: {{ test.endDate }}
          </v-card-text>
        </div>
      </div>
      <v-divider></v-divider>
      <v-card-text style="font-size: 1rem">
        {{ test.description }}
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <div v-if="validTest(test.startDate, test.endDate)">
          <v-btn v-if="!isGoingDoExam" @click="clickDoExam" color="primary" variant="elevated" style="font-weight: 500"
            :prepend-icon="test.private ? 'mdi-lock' : ''">
            Làm bài
          </v-btn>
          <v-btn v-else color="primary" variant="elevated" style="font-weight: 500">
            <v-progress-circular :width="3" :size="20" color="white" indeterminate></v-progress-circular>
          </v-btn>
        </div>
        <v-card-subtitle v-else style="color: red">Không trong thời gian làm bài!</v-card-subtitle>
      </v-card-actions>
    </v-card>
    <v-card v-if="histories.length" style="margin-top: 16px;">
      <v-card-title>Lịch sử làm bài</v-card-title>
      <v-card-text> <v-list style="padding-top: 0; padding-bottom: 0">
          <v-hover v-for="(testResult, idx) in histories" v-slot="{ isHovering, props }">
            <v-card :elevation="isHovering ? 8 : 4" v-bind="props" style="margin: 0 8px 16px">
              <v-list-item height="70" :key="testResult.id" :title="'Lần thi ' + (idx + 1)"
                :subtitle="testResult.created">
                <template v-slot:prepend>
                  <v-avatar color="success">
                    <v-icon color="white">mdi-clipboard-text</v-icon>
                  </v-avatar>
                </template>

                <template v-slot:append>
                  <div style="
                display: flex;
                flex-direction: column;
                align-items: flex-end;
              ">
                    <v-list-item-title v-if="!testResult.requiresGrading">Điểm: {{ testResult.score }}/{{
                      testResult.testDefaultScore
                    }}</v-list-item-title>
                  </div>
                </template>
              </v-list-item>
            </v-card>
          </v-hover>
        </v-list></v-card-text>
    </v-card>
  </div>
  <v-dialog v-model="isOpenPassword" max-width="500px">
    <v-card>
      <v-card-title> Đổi mật khẩu </v-card-title>
      <v-card-text>
        <v-row>
          <v-col cols="12" md="12" sm="12">
            <v-text-field type="password" v-model="password" label="Password"></v-text-field>
          </v-col>
        </v-row>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn variant="tonal" @click="cancelPassword"> Quay lại </v-btn>
        <v-btn color="success" variant="flat" @click="submitPassword">
          Làm bài
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import datetime from '@/utils/datetime';
import { getTestByClass, getTestByStudent, getTestResults } from '@/services';
import { mapState } from 'pinia';
import {
  authenticationRole,
  studentStore,
  testStore,
  toastStore,
} from '@/stores';

export default {
  data: () => ({
    test: {},
    isLoading: false,
    isGoingDoExam: false,
    password: '',
    isOpenPassword: false,
    histories: []
  }),
  computed: {
    ...mapState(authenticationRole, ['authentication']),
    ...mapState(studentStore, ['student']),
    ...mapState(testStore, ['updateTest']),
    ...mapState(toastStore, ['updateToast']),
  },
  async mounted() {
    this.isLoading = true;
    const res = await getTestByClass(
      this.authentication.accessToken.token,
      this.student.class.classId,
      this.$route.params.id
    );
    if (res.success) {
      this.test = res.data;
      const subres = await getTestResults(
        this.authentication.accessToken.token,
        this.student.class.classId,
        this.$route.params.id,
        this.student.studentId)
      if (subres.success) this.histories = subres.data.map(item => item.result)
    }
    this.isLoading = false;
  },
  methods: {
    cancelPassword() {
      this.password = '';
      this.isOpenPassword = false;
    },
    async submitPassword() {
      const res = await this.doExam();
      if (!res.success) {
        this.updateToast('error', 'Thất bại!');
      }
    },
    async clickDoExam() {
      if (this.test.private) {
        this.isOpenPassword = true;
      } else {
        await this.doExam();
      }
    },
    async doExam() {
      this.isGoingDoExam = true;
      const res = await getTestByStudent(
        this.authentication.accessToken.token,
        this.$route.params.id,
        this.student.class.classId,
        this.password
      );
      if (res.success) {
        this.updateTest(res.data);
      }
      this.isGoingDoExam = false;
      return res;
    },
    validTest(start, end) {
      return (
        datetime(start).value < new Date() && new Date() < datetime(end).value
      );
    },
  },
};
</script>
