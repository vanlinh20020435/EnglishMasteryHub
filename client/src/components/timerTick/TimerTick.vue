<template>
  <div class="real-timing font-semi-bold">
    {{ formattedTime.minutes }}:{{ formattedTime.seconds }}
  </div>
</template>

<script>
import { ref, reactive, watch } from 'vue';
import { formatTwoNumber } from '@/base/helper.js';
export default {
  props: {
    seconds: {
      type: Number,
      required: true
    },
    onEndTimerTick: {
      type: Function,
      required: true
    },
    disabled: {
      type: Boolean,
      default: false
    },
    onTimerTick: Function,
  },
  setup(props) {
    let currentSeconds = ref(props.seconds);
    let intervalId = null;

    const formattedTime = reactive({
      minutes: formatTwoNumber(Math.floor(currentSeconds.value / 60)),
      seconds: formatTwoNumber(currentSeconds.value % 60)
    });

    const updateTimer = () => {
      currentSeconds.value--;
      formattedTime.minutes = formatTwoNumber(Math.floor(currentSeconds.value / 60));
      formattedTime.seconds = formatTwoNumber(currentSeconds.value % 60);
      if (currentSeconds.value <= 0) {
        clearInterval(intervalId);
        if (typeof props.onEndTimerTick === 'function') {
          props.onEndTimerTick();
        }
      }
      if (typeof props.onTimerTick === 'function') {
        props.onTimerTick(currentSeconds.value);
      }
    };

    watch(() => props.seconds, (newValue) => {
      currentSeconds.value = newValue;
    });

    watch(() => props.disabled, (newValue) => {
      if (newValue) {
        clearInterval(intervalId);
      } else {
        clearInterval(intervalId);
        intervalId = setInterval(updateTimer, 1000);
      }
    });

    if (!props.disabled) {
      intervalId = setInterval(updateTimer, 1000);
    }

    // Clear interval on component unmount
    // onUnmounted(() => {
    //   clearInterval(intervalId);
    // });

    return {
      formattedTime
    };
  }
};

</script>

<style>
/* Your component styles here */
.real-timing {
  font-size: 1.125rem;
}
</style>