<template>
    <Audio :file="question.files[0].url" color="success"></Audio>
</template>

<script>
import Audio from '@/components/Audio.vue'

export default {
    components: {
        Audio
    },
    props: {
        question: Object
    }
}
</script>