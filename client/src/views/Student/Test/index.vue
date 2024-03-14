<template>
    <v-list>
        <v-hover v-for="test in tests" v-slot="{ isHovering, props }">
            <v-card @click="$router.replace(`/student/test/${test.testId}`)" :class="{ 'on-hover': isHovering }"
                :elevation="isHovering ? 4 : 2" v-bind="props" style="margin: 0 8px 8px">
                <v-list-item :key="test.testId" :subtitle="test.description" :title="test.testName">
                    <template v-slot:prepend>
                        <v-avatar color="success">
                            <v-icon color="white">mdi-clipboard-text</v-icon>
                        </v-avatar>
                    </template>

                    <template v-slot:append>
                        <div style="display: flex; flex-direction: column; align-items: flex-end;">
                            <v-list-item-title>Thời gian làm bài: {{ test.time }} phút</v-list-item-title>
                            <v-list-item-subtitle>Tổng số câu hỏi: {{ test.totalQuestions }} câu</v-list-item-subtitle>
                        </div>
                    </template>
                </v-list-item>
            </v-card>
        </v-hover>
    </v-list>
</template>

<script>
import { getTestsByClass } from '@/services'
import { mapState } from 'pinia';
import { authenticationRole, studentStore } from '@/stores';

export default {
    data: () => ({
        tests: [
            {
                "testId": 0,
                "testName": "Kiểm tra giữa kì",
                "password": "string",
                "totalQuestions": 0,
                "time": 0,
                "description": "Điểm giữa kì chiếm 40% tổng điểm học phần",
                "status": 0,
                "creator": "string",
                "havePermission": true,
                "questions": [
                    {
                        "questionId": 0,
                        "content": "string",
                        "description": "string",
                        "title": "string",
                        "type": "string",
                        "skill": "string",
                        "time": 0,
                        "subQuestions": [
                            "string"
                        ],
                        "answers": [
                            {
                                "answer": "string",
                                "explanation": "string"
                            }
                        ],
                        "options": [
                            {
                                "option": "string"
                            }
                        ],
                        "files": [
                            {
                                "id": 0,
                                "type": "string",
                                "url": "string",
                                "name": "string"
                            }
                        ]
                    }
                ],
                "created": "2024-03-11T17:42:12.246Z"
            },
            {
                "testId": 0,
                "testName": "string",
                "password": "string",
                "totalQuestions": 0,
                "time": 0,
                "description": "string",
                "status": 0,
                "creator": "string",
                "havePermission": true,
                "questions": [
                    {
                        "questionId": 0,
                        "content": "string",
                        "description": "string",
                        "title": "string",
                        "type": "string",
                        "skill": "string",
                        "time": 0,
                        "subQuestions": [
                            "string"
                        ],
                        "answers": [
                            {
                                "answer": "string",
                                "explanation": "string"
                            }
                        ],
                        "options": [
                            {
                                "option": "string"
                            }
                        ],
                        "files": [
                            {
                                "id": 0,
                                "type": "string",
                                "url": "string",
                                "name": "string"
                            }
                        ]
                    }
                ],
                "created": "2024-03-11T17:42:12.246Z"
            },
            {
                "testId": 0,
                "testName": "string",
                "password": "string",
                "totalQuestions": 0,
                "time": 0,
                "description": "string",
                "status": 0,
                "creator": "string",
                "havePermission": true,
                "questions": [
                    {
                        "questionId": 0,
                        "content": "string",
                        "description": "string",
                        "title": "string",
                        "type": "string",
                        "skill": "string",
                        "time": 0,
                        "subQuestions": [
                            "string"
                        ],
                        "answers": [
                            {
                                "answer": "string",
                                "explanation": "string"
                            }
                        ],
                        "options": [
                            {
                                "option": "string"
                            }
                        ],
                        "files": [
                            {
                                "id": 0,
                                "type": "string",
                                "url": "string",
                                "name": "string"
                            }
                        ]
                    }
                ],
                "created": "2024-03-11T17:42:12.246Z"
            }
        ]
    }),
    computed: {
        ...mapState(authenticationRole, ['authentication']),
        ...mapState(studentStore, ['student']),
    },
    async mounted() {
        console.log('test', this.authentication.accessToken.token, this.student.class.classId);
        const res = await getTestsByClass(this.authentication.accessToken.token, this.student.class.classId)
        if (res.success) {
            this.tests = res.data
        }
    }
}
</script>