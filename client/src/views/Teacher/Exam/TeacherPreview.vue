<template>
    <v-container>
        <HeaderTitle title="Preview" />
        <v-divider :thickness="16"></v-divider>
        <DoExam :preview="true"></DoExam>
    </v-container>
</template>

<script>
import HeaderTitle from "@/components/header/HeaderTitle.vue";
import DoExam from '@/views/Student/Test/TestSlug/DoExam/DoExam.vue';
export default {
    components: {
        DoExam,
        HeaderTitle,
    },
}</script>