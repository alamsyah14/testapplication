package com.sehatq.testapplication.core.widget

import android.annotation.SuppressLint
import android.content.Context
import android.os.SystemClock
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.sehatq.testapplication.R
import com.sehatq.testapplication.core.extention.*
import com.sehatq.testapplication.core.util.*
import kotlinx.android.synthetic.main.text_field_view.view.*

class TextField : ConstraintLayout {

    enum class FieldType {
        TEXT,
        SEARCH,
        DROP_DOWN,
        DATE_PICKER,
        CURRENCY,
        PASSWORD,
        NOT_EDITABLE
    }

    var errorText = ""
        set(value) {
            field = value
            error_txt.text = field
        }

    var text: String
        set(value) {
            edit_txt?.setText(value)
        }
        get() = edit_txt.text?.toString() ?: ""

    var hintTitle: String
        set(value) {
            hint_txt?.text = value
        }
        get() = hint_txt?.text.toString()

    var hintField: String
        set(value) {
            edit_txt?.hint = value
        }
        get() = edit_txt?.hint?.toString() ?: ""

    //variable hold value last time user click (to prevent double click)
    var lastTimeClick = 0L

    private val prefixCurrency = "Rp"

    private var textWatcher: TextWatcher? = null
    private var TextFieldInputFilter = InputFilterUtilities.filterTextField
    private var isPasswordVisible = false

    private var maximumLength = Int.MAX_VALUE
        @SuppressLint("SetTextI18n")
        set(value) {
            field = value
            counter_txt.text = "${edit_txt.text.length}/$maximumLength"
        }

    private var showCounterText = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    var allowAllCharacter = false
        set(isAllowed) {
            field = isAllowed
            if(isAllowed) {
                removeOneTextFieldCharInputFilter()
            }
        }

    @SuppressLint("SetTextI18n")
    protected fun init(attrs: AttributeSet? = null) {
        inflate(context,
            R.layout.text_field_view, this)
        setFieldType(FieldType.TEXT)
        var allCapsText = false
        attrs?.let {
            context.obtainStyledAttributes(it,
                R.styleable.TextField
            ).apply {
                try {
                    hint_txt.text = getString(R.styleable.TextField_titleHint)
                    edit_txt.hint = getString(R.styleable.TextField_fieldHint)
                    edit_txt.apply {
                        inputType = getInt(
                            R.styleable.TextField_android_inputType,
                            EditorInfo.TYPE_CLASS_TEXT
                        )
                        isFocusable = getBoolean(R.styleable.TextField_android_focusable, true)
                        allCapsText =
                            getBoolean(R.styleable.TextField_android_textAllCaps, false)
                    }
                    val fieldTypeValue = getInt(R.styleable.TextField_fieldType, 0)
                    setFieldType(FieldType.values()[fieldTypeValue])

                    val isShowLine = getBoolean(R.styleable.TextField_showLine, true)
                    showLine(isShowLine)

                    showCounterText = getBoolean(R.styleable.TextField_showCounter, false)
                    if (showCounterText) {
                        counter_txt.visible()
                        counter_txt.text = "${edit_txt.text.length}/$maximumLength"
                    } else {
                        counter_txt.invisible()
                    }
                } finally {
                    recycle()
                }
            }
        }

        if (allCapsText)
            addInputFilter(arrayOf(InputFilter.AllCaps()))

        addInputFilter(arrayOf(TextFieldInputFilter))
        edit_txt?.afterTextChanged {
            clearError()
        }
    }

    fun multiline() {
        edit_txt.setSingleLine(false)
    }

    fun singleLine() {
        edit_txt.setSingleLine(true)
    }

    fun removeTextWatcher() {
        textWatcher?.run {
            edit_txt?.removeTextChangedListener(textWatcher)
        }
    }

    /**
     * Hide Failed text
     */
    fun hideError() {
        error_txt.text = ""
    }

    /**
     * Show line under edit text
     */
    fun showLine(isShow: Boolean) {
        line_view?.visibility = if (isShow) View.VISIBLE else View.INVISIBLE
    }

    fun setInputType(inputType: Int) {
        if (EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS == inputType)
            removeOneTextFieldCharInputFilter()

        edit_txt.inputType = inputType or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
    }

    fun removeOneTextFieldCharInputFilter() {
        val currInputFilter = edit_txt?.filters?.toMutableList()
        currInputFilter?.remove(TextFieldInputFilter)
        edit_txt?.filters = currInputFilter?.toTypedArray()
    }

    /**
     * Set type of field, it should be SEARCH, DROP_DOWN, or TEXT. Default is text
     */
    fun setFieldType(type: FieldType) {
        when (type) {
            FieldType.SEARCH -> {
                setIconField(
                    R.drawable.ic_search
                )
                removeOneTextFieldCharInputFilter()
            }

            FieldType.DROP_DOWN -> {
                setIconField(R.drawable.ic_chevron_right_black)
                removeOneTextFieldCharInputFilter()
            }

            FieldType.DATE_PICKER -> {
                setIconField(R.drawable.ic_date)
                removeOneTextFieldCharInputFilter()
            }

            FieldType.PASSWORD -> {
                setIconField(R.drawable.ic_eye_hidden)
                setupPasswordTransformation()
            }

            FieldType.NOT_EDITABLE -> {
                setIconField(null)
                setupNotEditableTranformation()
                allowEdit(false)
            }
            else -> setIconField(null)
        }
    }

    private fun setupNotEditableTranformation() {
        edit_txt?.setPadding(
            getDimensionPx(R.dimen.dimen_8_dp),
            getDimensionPx(R.dimen.dimen_8_dp),
            getDimensionPx(R.dimen.dimen_8_dp),
            getDimensionPx(R.dimen.dimen_8_dp)
        )
        edit_txt?.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
        line_view?.setPadding(
            getDimensionPx(R.dimen.dimen_0_dp),
            getDimensionPx(R.dimen.dimen_0_dp),
            getDimensionPx(R.dimen.dimen_0_dp),
            getDimensionPx(R.dimen.dimen_8_dp)
        )
    }

    /**
     * Set drawable icon for edit text
     * Always add drawable in right of edit text
     */
    private fun setIconField(@DrawableRes drawableRes: Int?) {
        drawableRes?.let {
            val drawable = ContextCompat.getDrawable(context, it)
            edit_txt?.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
        }

    }

    /**
     * Set listener for the icon inside edit text. For now just action for icon search and drop down
     */
    @SuppressLint("ClickableViewAccessibility")
    fun setIconClickListener(listener: (View) -> Unit) {
        edit_txt?.setOnTouchListener { view, motionEvent ->
            // index drawable right in compoundDrawables.
            // 0 = left, 1 = top, 2 = right, 3 = bottom
            val drawableRight = 2
            val editText = view as EditText

            if (motionEvent.action != MotionEvent.ACTION_UP || editText.compoundDrawables[drawableRight] == null)
                return@setOnTouchListener false

            if (motionEvent.rawX >= (right - editText.compoundDrawables[drawableRight].bounds.width())) {
                listener(view)
                return@setOnTouchListener false
            }
            false

        }
    }

    /**
     * Set listener for  edit text. It will make edit text not focusable, why not focus? \n
     * Because by default edit text will trigger on focus change if isFocusable, so click listener will not called
     *
     */
    fun setClickListener(listener: (View) -> Unit) {
        if (edit_txt.isFocusable) edit_txt.isFocusable = false

        edit_txt.setOnClickListener {

            // Check if user double click field, 1s should be enough
            if (SystemClock.elapsedRealtime() - lastTimeClick < 1000)
                return@setOnClickListener

            listener(it)

            lastTimeClick = SystemClock.elapsedRealtime()
        }
    }

    /**
     * reset clickListener, esp. when you use it in recyclerView. When the x position previously the type was text, then
     * not text, and back to text again, we need to reset the focusable and the click listener
     */
    fun resetTextClickListener() {
        edit_txt?.isFocusable = true
        edit_txt?.isFocusableInTouchMode = true
        edit_txt?.setOnClickListener {}
    }

    fun allowEdit(isAllow: Boolean) {
        edit_txt?.apply {
            isFocusable = isAllow
            isClickable = isAllow
        }
    }

    @SuppressLint("SetTextI18n")
    fun setMaxLength(length: Int) {
        maximumLength = length
        addInputFilter(arrayOf(InputFilter.LengthFilter(length)))
    }

    fun addInputFilter(inputFilters: Array<InputFilter>) {
        val currInputFilter = edit_txt?.filters?.toMutableList()
        currInputFilter?.addAll(inputFilters)
        edit_txt?.filters = currInputFilter?.toTypedArray()
    }

    private fun clearError() {
        if (errorText.isNotBlank())
            errorText = ""
    }

    fun setSelection(index: Int) {
        edit_txt?.setSelection(index)
    }

    fun hideEditText() {
        edit_txt?.gone()
        showLine(false)
    }

    fun addTextWatcher(txtWatcher: TextWatcher) {
        if (textWatcher != null) removeTextWatcher()
        textWatcher = txtWatcher
        edit_txt?.addTextChangedListener(textWatcher)
    }

    private fun setupPasswordTransformation() {
        edit_txt.transformationMethod = PasswordTransformationMethod.getInstance()
        setIconClickListener {
            edit_txt.apply {
                transformationMethod = if (isPasswordVisible) {
                    setIconField(R.drawable.ic_eye_visible)
                    HideReturnsTransformationMethod.getInstance()
                } else {
                    setIconField(R.drawable.ic_eye_hidden)
                    PasswordTransformationMethod.getInstance()

                }
                invalidate()
            }

            isPasswordVisible = !isPasswordVisible
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun resetIconClickListener(){
        edit_txt.apply {
            setOnTouchListener { v, event -> true }
        }
    }

    private fun setText(textCurrency: String, textWatcher: TextWatcher) {
        removeTextWatcher()
        text = textCurrency
        setSelection(text.length)
        addTextWatcher(textWatcher)
    }


}