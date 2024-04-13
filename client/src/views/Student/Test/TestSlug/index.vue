<template>
    <DoExam v-if="isExam"></DoExam>
    <TestInfo v-else></TestInfo>
</template>

<script>
import TestInfo from './TestInfo.vue'
import DoExam from './DoExam/DoExam.vue';
import { authenticationRole, studentStore, testStore, toastStore } from '@/stores';
import { mapState } from 'pinia';
export default {
    components: {
        DoExam,
        TestInfo
    },
    computed: {
        ...mapState(testStore, ['test', 'clearTest']),
        isExam() {
            return this.test.testId
        }
    },
}</script>