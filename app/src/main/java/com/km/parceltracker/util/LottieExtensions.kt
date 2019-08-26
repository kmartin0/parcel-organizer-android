package com.km.parceltracker.util

import com.airbnb.lottie.LottieAnimationView

fun LottieAnimationView.playAnimation(animationEndListener : () -> Unit) {
    addAnimatorUpdateListener { animation ->
        if (animation.animatedFraction == 1F) animationEndListener()
    }
    playAnimation()
}