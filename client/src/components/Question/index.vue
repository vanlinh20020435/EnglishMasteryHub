<template>
    <Pronun1 v-if="question?.type === '1'" :question="question.subQuestions[0]"></Pronun1>
</template>

<script>
import Pronun1 from '@/components/Question/Pronun1.vue'
export default {
    components: {
        Pronun1
    },
    props: {
        question: Object
    },
    mounted() {
        console.log('hihihihihi: ', this.question);
    }
}
</script>