<template>
    test slug
</template>

<script>
import { getTest } from '@/services'
import { mapState } from 'pinia';
import { authenticationRole } from '@/stores';

export default {
    data: () => ({
        test: {}
    }),
    computed: {
        ...mapState(authenticationRole, ['authentication']),
    },
    async mounted() {
        const res = await getTest(this.authentication.accessToken.token, this.$route.params.id)
        if (res.success) {
            // this.tests = res.data
        }
    }
}
</script>