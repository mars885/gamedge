/*
 * Copyright 2020 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.paulrybitskyi.gamedge.commons.ui.widgets.chipflow

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.graphics.toRectF
import com.google.android.material.chip.ChipDrawable
import com.paulrybitskyi.commons.ktx.*
import com.paulrybitskyi.commons.material.utils.setChipBackgroundColor
import com.paulrybitskyi.commons.material.utils.setChipBackgroundCornerRadius
import com.paulrybitskyi.commons.material.utils.setChipIconColor
import com.paulrybitskyi.commons.utils.observeChanges
import com.paulrybitskyi.gamedge.commons.ui.widgets.R


private const val ITEM_BACKGROUND_SHADOW_RADIUS = 6f


class ChipFlowView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private val itemBackgroundShadowColor = getColor(R.color.chip_flow_item_background_shadow_color)
    private val itemIconStartPadding = getDimension(R.dimen.chip_flow_item_icon_start_padding)
    private val itemBackgroundShadowDx = getDimensionPixelSize(R.dimen.chip_flow_item_background_shadow_dx)
    private val itemBackgroundShadowDy = getDimensionPixelSize(R.dimen.chip_flow_item_background_shadow_dy)
    private val lastItemHeightCompensation = getDimensionPixelSize(R.dimen.chip_flow_last_item_height_compensation)
    private val defaultValues = initDefaultValues(context)

    @get:ColorInt
    var itemBackgroundColor = defaultValues.backgroundColor
        set(@ColorInt value) {
            field = value
            updateChipAttribute { it.setChipBackgroundColor(field) }
            invalidate()
        }

    @get:ColorInt
    var itemIconColor = defaultValues.iconColor
        set(@ColorInt value) {
            field = value
            updateChipAttribute { it.setChipIconColor(field) }
            invalidate()
        }

    var itemBackgroundCornerRadius = defaultValues.backgroundCornerRadius
        set(value) {
            field = value
            updateChipAttribute { it.setChipBackgroundCornerRadius(value) }
            invalidate()
        }

    var itemIconSize = defaultValues.iconSize
        set(value) {
            field = value
            updateChipAttribute { it.chipIconSize = field }
            requestLayout()
        }

    var itemTextAppearanceResource = defaultValues.textAppearanceResource
        set(value) {
            field = value
            updateChipAttribute { it.setTextAppearanceResource(value) }
            requestLayout()
        }

    var itemHorizontalSpacing = defaultValues.horizontalSpacing
        set(value) {
            field = value
            requestLayout()
        }

    var itemVerticalSpacing = defaultValues.verticalSpacing
        set(value) {
            field = value
            requestLayout()
        }

    private val itemBackgroundShadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = itemBackgroundShadowColor
        maskFilter = BlurMaskFilter(
            ITEM_BACKGROUND_SHADOW_RADIUS,
            BlurMaskFilter.Blur.NORMAL
        )
    }

    private var pressedChip: ChipDrawable? = null

    var items by observeChanges<List<ChipFlowItem>>(emptyList()) { oldItems, newItems ->
        if(newItems != oldItems) {
            initChips(newItems)
            requestLayout()
        }
    }

    private val chips = mutableListOf<ChipDrawable>()

    var onItemClicked: ((ChipFlowItem) -> Unit)? = null


    private fun initChips(items: List<ChipFlowItem>) {
        chips.clear()

        for(item in items) {
            chips.add(initChip(item))
        }
    }


    private fun initChip(chipItem: ChipFlowItem): ChipDrawable {
        return ChipDrawable.createFromAttributes(
            context,
            null,
            com.google.android.material.R.attr.chipStandaloneStyle,
            com.google.android.material.R.style.Widget_MaterialComponents_Chip_Entry
        ).apply {
            useCompatRipple = true
            isChipIconVisible = (chipItem.iconId != null)
            isCloseIconVisible = false
            isCheckedIconVisible = false
            ellipsize = TextUtils.TruncateAt.END
            setChipBackgroundColor(itemBackgroundColor)
            setChipIconColor(itemIconColor)
            chipIconSize = itemIconSize
            iconStartPadding = itemIconStartPadding
            textStartPadding = textEndPadding
            setChipBackgroundCornerRadius(itemBackgroundCornerRadius)
            setTextAppearanceResource(itemTextAppearanceResource)
            chipIcon = chipItem.iconId?.let(::getDrawable)
            text = chipItem.text
            callback = this@ChipFlowView
        }
    }


    private fun updateChipAttribute(action: (ChipDrawable) -> Unit) {
        if(chips.isEmpty()) return

        chips.forEach(action)
    }


    override fun verifyDrawable(who: Drawable): Boolean {
        return (super.verifyDrawable(who) || (pressedChip == who))
    }


    override fun drawableStateChanged() {
        super.drawableStateChanged()

        pressedChip?.state = drawableState
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val availableWidth = MeasureSpec.getSize(widthMeasureSpec)
        val calculatedHeight = measureChips(availableWidth)
        val newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
            calculatedHeight,
            MeasureSpec.EXACTLY
        )

        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec)
    }


    private fun measureChips(availableWidth: Int): Int {
        var chipWidth: Int
        var chipHorizontalSpacing: Int
        var chipLeft: Int
        var chipTop: Int
        var chipRight: Int
        var chipBottom: Int

        val chipShadowDx = itemBackgroundShadowDx
        val chipShadowDy = itemBackgroundShadowDy
        val chipTotalShadowDx = (chipShadowDx * 2)
        val chipTotalShadowDy = (chipShadowDy * 2)
        var chipHeight = 0

        val initialWidth = chipShadowDx
        val initialHeight = chipShadowDy

        var takenWidth = initialWidth
        var takenHeight = initialHeight

        var newWidth: Int

        for(chip in chips) {
            chipWidth = (if(chip.intrinsicWidth > availableWidth) availableWidth else chip.intrinsicWidth)
            chipHeight = chip.intrinsicHeight
            chipHorizontalSpacing = (if(takenWidth > initialWidth) itemHorizontalSpacing else 0)
            newWidth = (takenWidth + chipWidth + chipTotalShadowDx + chipHorizontalSpacing)

            if(newWidth > availableWidth) {
                takenWidth = initialWidth
                takenHeight += (chipHeight + chipTotalShadowDy + itemVerticalSpacing)
                chipHorizontalSpacing = 0
            }

            chipLeft = (takenWidth + chipShadowDx + chipHorizontalSpacing)
            chipTop = (takenHeight + chipShadowDy)
            chipRight = (chipLeft + chipWidth)
            chipBottom = (chipTop + chipHeight)

            chip.applyBounds(chipLeft, chipTop, chipRight, chipBottom)

            takenWidth = chipRight
        }

        return (takenHeight + chipHeight + lastItemHeightCompensation)
    }


    override fun onDraw(canvas: Canvas) = with(canvas) {
        for(chip in chips) {
            drawShadow(chip)
            drawChip(chip)
        }
    }


    private fun Canvas.drawShadow(chip: ChipDrawable) {
        val chipBounds = chip.bounds.toRectF()

        drawRoundRect(
            (chipBounds.left - itemBackgroundShadowDx),
            (chipBounds.top + itemBackgroundShadowDy),
            (chipBounds.right + itemBackgroundShadowDx),
            (chipBounds.bottom + itemBackgroundShadowDy),
            chip.chipCornerRadius,
            chip.chipCornerRadius,
            itemBackgroundShadowPaint
        )
    }


    private fun Canvas.drawChip(chip: ChipDrawable) {
        chip.draw(this)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> onPointerDown(event)
            MotionEvent.ACTION_MOVE -> onPointerMoved(event)

            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> onPointerReleased(event)
        }

        return true
    }


    private fun onPointerDown(event: MotionEvent) {
        pressedChip = findPressedChip(event.x, event.y)
        isPressed = true
    }


    private fun onPointerMoved(event: MotionEvent) {
        if(pressedChip?.bounds?.contains(event.x.toInt(), event.y.toInt()) == false) {
            isPressed = false
            pressedChip = null
        }
    }


    private fun onPointerReleased(event: MotionEvent) {
        if((event.action == MotionEvent.ACTION_UP) && (pressedChip != null)) {
            val clickedChip = checkNotNull(pressedChip)
            val chipItem = findItemForChip(clickedChip)

            chipItem?.let(::onClickDetected)
        }

        isPressed = false
        pressedChip = null
    }


    private fun findPressedChip(eventX: Float, eventY: Float): ChipDrawable? {
        return chips.firstOrNull {
            it.bounds.contains(eventX.toInt(), eventY.toInt())
        }
    }


    private fun findItemForChip(chip: ChipDrawable): ChipFlowItem? {
        return chips
            .indexOfFirstOrNull { it == chip }
            ?.let(items::getOrNull)
    }


    private fun onClickDetected(item: ChipFlowItem) {
        onItemClicked?.invoke(item)
    }


}