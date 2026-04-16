package com.example.darshanai

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import com.example.darshanai.ui.theme.DarshanAiTheme
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.util.Properties
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import kotlin.random.Random
import java.io.ByteArrayOutputStream
import javax.mail.util.ByteArrayDataSource
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.foundation.Image
import java.util.Date
import java.util.concurrent.TimeUnit
import android.widget.Toast
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

// ─── Theme Management ────────────────────────────────────────────────────────────
data class ThemeColors(
    val background: Color,
    val surface: Color,
    val card: Color,
    val primary: Color,
    val primaryLight: Color,
    val primaryDark: Color,
    val accent: Color,
    val success: Color,
    val warning: Color,
    val text: Color,
    val textSecondary: Color,
    val gradientStart: Color,
    val gradientEnd: Color
)

object ThemeManager {
    private const val PREFS_NAME = "theme_prefs"
    private const val KEY_IS_DARK_THEME = "is_dark_theme"

    fun isDarkTheme(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_IS_DARK_THEME, true)
    }

    fun setDarkTheme(context: Context, isDark: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_IS_DARK_THEME, isDark).apply()
    }
}

val DarkTheme = ThemeColors(
    background = Color(0xFF0A0E17),
    surface = Color(0xFF111827),
    card = Color(0xFF1F2937),
    primary = Color(0xFF8B5CF6),
    primaryLight = Color(0xFFA78BFA),
    primaryDark = Color(0xFF6D28D9),
    accent = Color(0xFFEC4899),
    success = Color(0xFF10B981),
    warning = Color(0xFFF59E0B),
    text = Color(0xFFF3F4F6),
    textSecondary = Color(0xFF9CA3AF),
    gradientStart = Color(0xFF8B5CF6),
    gradientEnd = Color(0xFFEC4899)
)

val LightTheme = ThemeColors(
    background = Color(0xFFF8FAFC),
    surface = Color(0xFFF1F5F9),
    card = Color(0xFFE2E8F0),
    primary = Color(0xFF8B5CF6),
    primaryLight = Color(0xFFA78BFA),
    primaryDark = Color(0xFF6D28D9),
    accent = Color(0xFFEC4899),
    success = Color(0xFF10B981),
    warning = Color(0xFFF59E0B),
    text = Color(0xFF1E293B),
    textSecondary = Color(0xFF475569),
    gradientStart = Color(0xFF8B5CF6),
    gradientEnd = Color(0xFFEC4899)
)

// ─── Enhanced Temple Data Class with Full Details ────────────────────────────────────────────────────────
data class Temple(
    val name: String,
    val imageRes: Int,
    val crowd: Int,
    val waitTime: String,
    val rating: Double,
    val description: String,
    val fullDescription: String,
    val story: String,
    val location: String,
    val address: String,
    val openingHours: String,
    val bestTimeToVisit: String,
    val festivals: List<String>,
    val architecture: String,
    val howToReach: String,
    val nearbyAttractions: List<String>,
    val tips: List<String>
)

// ─── Temple Data Manager ───────────────────────────────────────────────────────────
object TempleDataManager {
    fun getAllTemples(): List<Temple> {
        return listOf(
            Temple(
                name = "Somnath",
                imageRes = R.drawable.ic_launcher_background,
                crowd = 85,
                waitTime = "45 mins",
                rating = 4.8,
                description = "One of the 12 Jyotirlinga shrines, located on the western coast of Gujarat",
                fullDescription = "The Somnath Temple, also known as the 'Shrine Eternal', is one of the most revered pilgrimage sites in India. It is the first among the twelve Jyotirlinga shrines of Lord Shiva. The temple has been destroyed and rebuilt sixteen times, symbolizing the resilience of faith. The current temple was reconstructed in 1951 in the Chalukya style of architecture.",
                story = "According to legend, Somnath was originally built by the Moon God (Soma) himself. The story goes that Lord Chandra (the Moon God) was cursed by his father-in-law Daksha Prajapati to lose his luster. The curse could only be nullified if Chandra worshipped Lord Shiva at this sacred site. After performing intense penance, Lord Shiva blessed Chandra and restored his light. Hence, the name 'Somnath' - 'Soma' means Moon God and 'Nath' means Lord, making it 'The Lord of the Moon'. The temple has witnessed numerous invasions, including Mahmud of Ghazni's raid in 1024 AD, but each time it was rebuilt with even greater splendor.",
                location = "Prabhas Patan, Veraval, Gujarat",
                address = "Somnath Mandir Road, Somnath, Veraval, Gujarat 362268",
                openingHours = "6:00 AM - 9:00 PM (Daily)\nAarti Timings: 7:00 AM, 12:00 PM, 7:00 PM",
                bestTimeToVisit = "October to March (Winter months)\nMorning hours (7 AM - 10 AM) for peaceful darshan",
                festivals = listOf(
                    "Maha Shivaratri (February-March)",
                    "Shravan Month (July-August)",
                    "Kartik Purnima (November)",
                    "Somnath Festival (December)"
                ),
                architecture = "The temple is built in the Chalukyan style of architecture, featuring intricate carvings and sculptures. The main spire (Shikhara) rises to a height of 155 feet. The temple faces east and is situated at such a point that there is no land between Somnath and Antarctica. The flag atop the temple is changed three times daily and can be seen from miles away.",
                howToReach = "✈️ Air: Nearest airport is Diu Airport (85 km) or Rajkot Airport (200 km)\n🚂 Rail: Somnath Railway Station (5 km) is well-connected\n🚌 Road: Regular buses from major cities of Gujarat\n🚗 Taxi: Easily available from Veraval (5 km)",
                nearbyAttractions = listOf(
                    "Bhalka Tirtha - Where Lord Krishna was mistaken as a deer",
                    "Triveni Ghat - Confluence of three holy rivers",
                    "Patan Gate",
                    "Somnath Museum",
                    "Gita Mandir",
                    "Chorwad Beach (25 km)"
                ),
                tips = listOf(
                    "Remove shoes before entering the main temple",
                    "Mobile phones are not allowed inside",
                    "Best to visit during morning aarti for spiritual experience",
                    "Book VIP darshan tickets online to avoid queues",
                    "Visit during sunset for spectacular views",
                    "Dress modestly (cover shoulders and knees)"
                )
            ),
            Temple(
                name = "Dwarka",
                imageRes = R.drawable.ic_launcher_background,
                crowd = 65,
                waitTime = "25 mins",
                rating = 4.7,
                description = "Ancient city of Lord Krishna, situated on the banks of river Gomti",
                fullDescription = "Dwarkadhish Temple, also known as Jagat Mandir, is one of the Char Dham pilgrimage sites. Built around 400 BC, this magnificent temple is dedicated to Lord Krishna. The temple stands on the banks of the Gomti River and features a five-story shikhara (spire) supported by 72 pillars.",
                story = "Dwarka was the legendary capital of Lord Krishna's kingdom. According to the Mahabharata, Lord Krishna established this city after leaving Mathura. The city was built by Vishwakarma (the divine architect) on the orders of Lord Krishna. It was believed to be a golden city that stretched 84 km along the coast. After Krishna's departure from Earth, the city is said to have submerged into the sea. Archaeological discoveries have found underwater ruins confirming the existence of an ancient city.",
                location = "Dwarka, Gujarat",
                address = "Dwarkadhish Temple, Dwarka, Gujarat 361335",
                openingHours = "6:30 AM - 1:00 PM, 5:00 PM - 9:30 PM\nAarti Timings: 7:00 AM, 12:00 PM, 7:00 PM",
                bestTimeToVisit = "October to March\nJanmashtami (August-September) is especially auspicious",
                festivals = listOf(
                    "Janmashtami (August-September)",
                    "Dwarka Festival (February)",
                    "Sharad Purnima (October)",
                    "Holi (March)"
                ),
                architecture = "The temple is built in the Chalukya style with a five-story shikhara. The main temple is constructed of limestone and sand. The black marble idol of Lord Krishna is the main attraction. The temple complex includes many smaller shrines dedicated to various deities.",
                howToReach = "✈️ Air: Jamnagar Airport (137 km)\n🚂 Rail: Dwarka Railway Station (2 km)\n🚌 Road: Regular buses from Jamnagar, Rajkot, Ahmedabad\n🚢 Boat: Ferry service to Bet Dwarka available",
                nearbyAttractions = listOf(
                    "Bet Dwarka - Krishna's residence",
                    "Nageshwar Jyotirlinga Temple",
                    "Rukmini Devi Temple",
                    "Gomti Ghat",
                    "Dwarka Beach",
                    "Sudama Setu (Bridge)",
                    "Bhadkeshwar Mahadev Temple"
                ),
                tips = listOf(
                    "Visit Bet Dwarka via ferry (30 minutes journey)",
                    "Take a holy dip at Gomti Ghat",
                    "Attend evening aarti for beautiful experience",
                    "Try local Gujarati thali at temple canteen",
                    "Hire a guide for historical insights"
                )
            ),
            Temple(
                name = "Ambaji",
                imageRes = R.drawable.ic_launcher_background,
                crowd = 45,
                waitTime = "10 mins",
                rating = 4.6,
                description = "Famous Shakti Peetha in the Aravalli hills of Gujarat",
                fullDescription = "Ambaji Temple is one of the 51 Shakti Peethas, dedicated to Goddess Amba. It is considered the most important Shakti Peetha in Gujarat. The temple doesn't have an idol but houses the holy Sri Yantra, which is worshipped as the goddess's symbolic representation.",
                story = "According to legend, when Lord Shiva was carrying the burning body of Sati, her heart fell at this very spot. The temple is built on the site where Sati's heart is believed to have fallen. The original temple is said to have been built by Lord Brahma himself. The sacred flame (Akhand Jyoti) has been burning continuously for centuries without any fuel source, which devotees consider miraculous.",
                location = "Ambaji, Banaskantha district, Gujarat",
                address = "Ambaji Temple, Ambaji, Gujarat 385110",
                openingHours = "5:00 AM - 11:30 PM (Daily)",
                bestTimeToVisit = "October to February\nNavratri (September-October) is the best time",
                festivals = listOf(
                    "Navratri (September-October)",
                    "Bhadarvi Purnima (August-September)",
                    "Maha Shivaratri (February-March)",
                    "Diwali (October-November)"
                ),
                architecture = "The temple features exquisite marble work and intricate carvings. The main sanctum houses the Sri Yantra, which is considered very powerful. The temple has been renovated multiple times but maintains its original spiritual significance.",
                howToReach = "✈️ Air: Ahmedabad Airport (165 km)\n🚂 Rail: Abu Road Railway Station (45 km)\n🚌 Road: Regular buses from Ahmedabad, Mount Abu, Palanpur\n🚗 Taxi: Easily available from Abu Road",
                nearbyAttractions = listOf(
                    "Gabbar Hill - Sacred hill with temple",
                    "Kumbhariya Jain Temples",
                    "Mount Abu (45 km)",
                    "Balaram Palace",
                    "Sitanu Waterfall"
                ),
                tips = listOf(
                    "Climb Gabbar Hill (1000 steps) for panoramic views",
                    "Visit during Navratri for grand celebrations",
                    "Try local sweets at temple prasad counters",
                    "Stay overnight for evening aarti",
                    "Carry water while climbing the hill"
                )
            ),
            Temple(
                name = "Pavagadh",
                imageRes = R.drawable.ic_launcher_background,
                crowd = 90,
                waitTime = "60 mins",
                rating = 4.5,
                description = "UNESCO World Heritage site with ancient Jain temples",
                fullDescription = "Pavagadh is a magnificent hill station and archaeological site featuring ancient temples, including the famous Kalika Mata Temple. The site is a UNESCO World Heritage Site and contains significant historical structures from the 8th to 14th centuries.",
                story = "Legend says that Pavagadh Hill is a part of the Himalayas that was moved here by Hanuman. The Kalika Mata Temple at the top is believed to be where Goddess Kali appeared to protect the devotees. The hill has witnessed several battles and changed hands between Hindu, Jain, and Muslim rulers, each leaving their architectural mark.",
                location = "Pavagadh, Panchmahal district, Gujarat",
                address = "Pavagadh Temple, Pavagadh, Halol, Gujarat 389360",
                openingHours = "5:00 AM - 7:00 PM (Daily)\nRopeway: 8:00 AM - 6:00 PM",
                bestTimeToVisit = "October to March\nEarly morning (5 AM - 8 AM) to avoid crowds",
                festivals = listOf(
                    "Chaitra Purnima (March-April)",
                    "Sharad Purnima (October)",
                    "Navratri (September-October)",
                    "Pavagadh Festival (December)"
                ),
                architecture = "The site combines Hindu, Jain, and Islamic architecture. Key structures include the Kalika Mata Temple (10th century), Jain temples with intricate marble carvings, and the 15th-century Jami Masjid. The fort walls and stepwells showcase medieval Indian engineering.",
                howToReach = "✈️ Air: Vadodara Airport (45 km)\n🚂 Rail: Champaner Railway Station (5 km) or Vadodara Junction\n🚌 Road: Regular buses from Vadodara, Ahmedabad\n🚠 Ropeway: Available from base to hilltop",
                nearbyAttractions = listOf(
                    "Champaner Archaeological Park",
                    "Jami Masjid - UNESCO World Heritage",
                    "Stepwells (Vavs)",
                    "Sat Kaman (Seven Arches)",
                    "Halol City",
                    "Jain Derasar"
                ),
                tips = listOf(
                    "Take ropeway to save energy for temple exploration",
                    "Wear comfortable shoes for walking",
                    "Start early to avoid afternoon heat",
                    "Visit the stepwells for unique architecture",
                    "Carry sunscreen and hat",
                    "Weekdays are less crowded"
                )
            )
        )
    }
}

// ─── Session Management ──────────────────────────────────────────────────────
object SessionManager {
    private lateinit var prefs: SharedPreferences
    private const val PREF_NAME = "DarshanAI_Prefs"
    private const val KEY_USER_SESSION = "user_session"
    private const val KEY_BOOKING_HISTORY = "booking_history"
    private const val SESSION_DURATION_HOURS = 24L
    private const val SESSION_DURATION_MS = SESSION_DURATION_HOURS * 60 * 60 * 1000

    fun init(context: Context) {
        if (!::prefs.isInitialized) {
            prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        }
    }

    fun saveUserSession(email: String) {
        val session = UserSession(
            email = email,
            loginTimestamp = System.currentTimeMillis(),
            isLoggedIn = true
        )
        val gson = Gson()
        val json = gson.toJson(session)
        prefs.edit().putString(KEY_USER_SESSION, json).apply()
    }

    fun getUserSession(): UserSession? {
        val json = prefs.getString(KEY_USER_SESSION, null)
        return if (json != null) {
            val type = object : TypeToken<UserSession>() {}.type
            Gson().fromJson(json, type)
        } else {
            null
        }
    }

    fun isSessionValid(): Boolean {
        val session = getUserSession() ?: return false
        if (!session.isLoggedIn) return false
        val currentTime = System.currentTimeMillis()
        val sessionAge = currentTime - session.loginTimestamp
        return sessionAge < SESSION_DURATION_MS
    }

    fun getUser(): String? {
        val session = getUserSession()
        return if (session != null && isSessionValid()) session.email else null
    }

    fun getUserInitial(): String {
        val email = getUser() ?: return "?"
        val firstChar = email.firstOrNull()?.toString() ?: "?"
        val charStr = firstChar.uppercase()
        return if (charStr.matches(Regex("[A-Z]"))) charStr else "U"
    }

    fun getSessionRemainingTime(): Long {
        val session = getUserSession() ?: return 0
        val currentTime = System.currentTimeMillis()
        val elapsed = currentTime - session.loginTimestamp
        val remaining = SESSION_DURATION_MS - elapsed
        return if (remaining > 0) remaining else 0
    }

    fun getFormattedRemainingTime(): String {
        val remainingMs = getSessionRemainingTime()
        if (remainingMs <= 0) return "Expired"
        val hours = TimeUnit.MILLISECONDS.toHours(remainingMs)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(remainingMs) % 60
        return when {
            hours > 0 -> "$hours hr ${minutes} min"
            minutes > 0 -> "$minutes min"
            else -> "Less than a minute"
        }
    }

    fun clearSession() {
        prefs.edit().remove(KEY_USER_SESSION).apply()
        EmailService.clearCache()
    }

    fun isLoggedIn(): Boolean {
        return isSessionValid()
    }

    private fun getHistoryKey(): String {
        return "${KEY_BOOKING_HISTORY}_${getUser() ?: "guest"}"
    }

    fun saveBooking(booking: BookingDetails) {
        val history = getBookingHistory().toMutableList()
        history.add(0, booking)
        if (history.size > 50) history.removeAt(history.size - 1)
        val gson = Gson()
        val json = gson.toJson(history)
        prefs.edit().putString(getHistoryKey(), json).apply()
    }

    fun getBookingHistory(): List<BookingDetails> {
        val json = prefs.getString(getHistoryKey(), null)
        return if (json != null) {
            val type = object : TypeToken<List<BookingDetails>>() {}.type
            Gson().fromJson(json, type)
        } else {
            emptyList()
        }
    }

    fun clearHistory() {
        prefs.edit().remove(getHistoryKey()).apply()
    }
}

// ─── Data Classes ────────────────────────────────────────────────────────
data class Yatri(
    val id: Int,
    var name: String,
    var age: String,
    var gender: String
)

data class BookingDetails(
    val templeName: String,
    val date: String,
    val timeSlot: String,
    val Yatris: List<Yatri>,
    val bookingId: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class UserSession(
    val email: String,
    val loginTimestamp: Long,
    val isLoggedIn: Boolean = true
)

// ─── Optimized QR Code Generator ───────────────────────────────────────────────────────
object QRCodeGenerator {
    fun generateQRBitmap(data: String, size: Int = 400): Bitmap {
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()

        canvas.drawColor(android.graphics.Color.WHITE)

        val cellSize = size / 25f
        val dataHash = data.hashCode()

        paint.color = android.graphics.Color.BLACK

        canvas.drawRect(0f, 0f, cellSize * 7, cellSize * 7, paint)
        canvas.drawRect(cellSize, cellSize, cellSize * 6, cellSize * 6, paint)
        paint.color = android.graphics.Color.WHITE
        canvas.drawRect(cellSize * 2, cellSize * 2, cellSize * 5, cellSize * 5, paint)
        paint.color = android.graphics.Color.BLACK

        canvas.drawRect(size - cellSize * 7, 0f, size.toFloat(), cellSize * 7, paint)
        canvas.drawRect(size - cellSize * 6, cellSize, size - cellSize, cellSize * 6, paint)
        paint.color = android.graphics.Color.WHITE
        canvas.drawRect(size - cellSize * 5, cellSize * 2, size - cellSize * 2, cellSize * 5, paint)
        paint.color = android.graphics.Color.BLACK

        canvas.drawRect(0f, size - cellSize * 7, cellSize * 7, size.toFloat(), paint)
        canvas.drawRect(cellSize, size - cellSize * 6, cellSize * 6, size - cellSize, paint)
        paint.color = android.graphics.Color.WHITE
        canvas.drawRect(cellSize * 2, size - cellSize * 5, cellSize * 5, size - cellSize * 2, paint)
        paint.color = android.graphics.Color.BLACK

        val absHash = kotlin.math.abs(dataHash)
        for (i in 0 until 400) {
            val x = (i % 20) * cellSize
            val y = (i / 20) * cellSize
            val shouldDraw = (absHash and (1 shl (i % 30))) != 0
            if (shouldDraw && x > cellSize * 8 && x < size - cellSize * 8) {
                canvas.drawRect(x, y, x + cellSize, y + cellSize, paint)
            }
        }

        return bitmap
    }
}

// ─── Optimized Email Service ────────────────────────────────────────────────────
object EmailService {
    private const val EMAIL = "Maheshwaridiya6@gmail.com"
    private const val PASSWORD = "bjyf fezy qsxc fkkm"

    private val props = Properties().apply {
        put("mail.smtp.auth", "true")
        put("mail.smtp.starttls.enable", "true")
        put("mail.smtp.host", "smtp.gmail.com")
        put("mail.smtp.port", "587")
        put("mail.smtp.ssl.trust", "smtp.gmail.com")

        put("mail.smtp.connectiontimeout", "5000")
        put("mail.smtp.timeout", "10000")
        put("mail.smtp.writetimeout", "10000")
        put("mail.smtp.poolsize", "2")
        put("mail.smtp.pooling", "true")
        put("mail.debug", "false")
        put("mail.smtp.ssl.protocols", "TLSv1.2")
    }

    private val session: Session by lazy {
        Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(EMAIL, PASSWORD)
            }
        }).apply {
            debug = false
        }
    }

    private val emailDispatcher = Dispatchers.IO.limitedParallelism(2)
    private val qrImageCache = mutableMapOf<String, ByteArray>()
    private var cachedOtpTemplate: String? = null

    suspend fun sendOTPEmail(recipientEmail: String, otp: String): Boolean {
        return withContext(emailDispatcher) {
            try {
                val message = MimeMessage(session).apply {
                    setFrom(InternetAddress(EMAIL))
                    setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail))
                    setSubject("DarshanAI - Your OTP Verification Code")
                    setContent(getOtpHtmlTemplate(otp), "text/html; charset=utf-8")
                }
                Transport.send(message)
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun sendBookingConfirmation(
        recipientEmail: String,
        bookingDetails: BookingDetails,
        qrBitmap: Bitmap
    ): Boolean {
        return withContext(emailDispatcher) {
            try {
                val message = MimeMessage(session).apply {
                    setFrom(InternetAddress(EMAIL))
                    setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail))
                    setSubject("DarshanAI - Booking Confirmation - ${bookingDetails.bookingId}")
                }

                val multipart = MimeMultipart("mixed")

                val htmlPart = MimeBodyPart().apply {
                    setContent(getBookingHtmlTemplate(bookingDetails), "text/html; charset=utf-8")
                }
                multipart.addBodyPart(htmlPart)

                val imageData = getCompressedQRImage(bookingDetails.bookingId, qrBitmap)
                val imagePart = MimeBodyPart().apply {
                    val dataSource = ByteArrayDataSource(imageData, "image/jpeg")
                    dataHandler = javax.activation.DataHandler(dataSource)
                    fileName = "darshan_qr_${bookingDetails.bookingId}.jpg"
                    setHeader("Content-ID", "<qr_code>")
                }
                multipart.addBodyPart(imagePart)

                message.setContent(multipart)
                Transport.send(message)
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    private fun getCompressedQRImage(bookingId: String, qrBitmap: Bitmap): ByteArray {
        return qrImageCache.getOrPut(bookingId) {
            val baos = ByteArrayOutputStream()
            qrBitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
            baos.toByteArray()
        }
    }

    private fun getOtpHtmlTemplate(otp: String): String {
        return cachedOtpTemplate ?: buildString {
            append("""
            <html>
            <body style="font-family:Arial,sans-serif;margin:0;padding:20px;background-color:#0A0E17;">
                <div style="max-width:500px;margin:0 auto;background:linear-gradient(135deg,#1F2937,#111827);border-radius:20px;padding:30px;">
                    <div style="text-align:center;">
                        <h1 style="background:linear-gradient(135deg,#8B5CF6,#EC4899);-webkit-background-clip:text;-webkit-text-fill-color:transparent;">🛕 DarshanAI</h1>
                        <h2 style="color:#F3F4F6;">Your Verification Code</h2>
                    </div>
                    <div style="background:#374151;padding:20px;border-radius:15px;margin:20px 0;text-align:center;">
                        <p style="font-size:42px;font-weight:bold;letter-spacing:5px;background:linear-gradient(135deg,#8B5CF6,#EC4899);-webkit-background-clip:text;-webkit-text-fill-color:transparent;margin:10px 0;">$otp</p>
                        <p style="font-size:12px;color:#9CA3AF;">Valid for 60 seconds</p>
                    </div>
                </div>
            </body>
            </html>
            """.trimIndent())
        }.also { cachedOtpTemplate = it }.replace("$otp", otp)
    }

    private fun getBookingHtmlTemplate(bookingDetails: BookingDetails): String {
        val yatriTable = buildString {
            bookingDetails.Yatris.forEach { yatri ->
                append("""
                <tr style="border-bottom:1px solid #374151;">
                    <td style="padding:8px;color:#F3F4F6;">${escapeHtml(yatri.name)}</td>
                    <td style="padding:8px;color:#F3F4F6;">${yatri.age}</td>
                    <td style="padding:8px;color:#F3F4F6;">${yatri.gender}</td>
                 </tr>
                """.trimIndent())
            }
        }

        return """
        <html>
        <body style="font-family:Arial,sans-serif;margin:0;padding:0;background-color:#0A0E17;">
            <div style="max-width:600px;margin:20px auto;background:linear-gradient(135deg,#1F2937,#111827);border-radius:20px;overflow:hidden;">
                <div style="background:linear-gradient(135deg,#8B5CF6,#EC4899);padding:30px;text-align:center;">
                    <h1 style="color:white;margin:0;">🛕 DarshanAI</h1>
                    <p style="color:rgba(255,255,255,0.8);margin-top:10px;">Booking Confirmation</p>
                </div>
                <div style="padding:30px;">
                    <div style="text-align:center;margin-bottom:30px;">
                        <div style="background:#374151;border-radius:10px;padding:15px;">
                            <p style="color:#9CA3AF;">Booking ID</p>
                            <p style="font-size:24px;font-weight:bold;background:linear-gradient(135deg,#8B5CF6,#EC4899);-webkit-background-clip:text;-webkit-text-fill-color:transparent;">${bookingDetails.bookingId}</p>
                        </div>
                    </div>
                    <div style="background:#374151;border-radius:10px;padding:20px;margin-bottom:20px;">
                        <h3 style="color:#8B5CF6;">📍 Temple Details</h3>
                        <p style="color:#F3F4F6;"><strong>Temple:</strong> ${escapeHtml(bookingDetails.templeName)}</p>
                        <p style="color:#F3F4F6;"><strong>Date:</strong> ${bookingDetails.date}</p>
                        <p style="color:#F3F4F6;"><strong>Time Slot:</strong> ${bookingDetails.timeSlot}</p>
                    </div>
                    <div style="background:#374151;border-radius:10px;padding:20px;">
                        <h3 style="color:#8B5CF6;">👥 Yatri Details</h3>
                        <table style="width:100%;border-collapse:collapse;">
                            <thead>
                                <tr style="background:linear-gradient(135deg,#8B5CF6,#EC4899);">
                                    <th style="padding:10px;text-align:left;color:white;">Name</th>
                                    <th style="padding:10px;text-align:left;color:white;">Age</th>
                                    <th style="padding:10px;text-align:left;color:white;">Gender</th>
                                 </tr>
                            </thead>
                            <tbody>
                                $yatriTable
                            </tbody>
                         </table>
                    </div>
                    <div style="text-align:center;margin-top:20px;">
                        <p style="color:#9CA3AF;"><img src="cid:qr_code" width="150" height="150" alt="QR Code" style="border-radius:10px;"></p>
                        <p style="font-size:12px;color:#6B7280;">Show this QR code at temple entrance</p>
                    </div>
                </div>
            </div>
        </body>
        </html>
        """.trimIndent()
    }

    private fun escapeHtml(input: String): String {
        return input
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;")
    }

    fun clearCache() {
        qrImageCache.clear()
    }
}

// ─── Activity ────────────────────────────────────────────────────────────────
class Mainpage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        createNotificationChannel()
        SessionManager.init(applicationContext)

        setContent {
            val isDarkTheme = remember { mutableStateOf(ThemeManager.isDarkTheme(this)) }

            DarshanAiTheme(
                darkTheme = isDarkTheme.value
            ) {
                AppNav(
                    isDarkTheme = isDarkTheme.value,
                    onThemeToggle = {
                        isDarkTheme.value = !isDarkTheme.value
                        ThemeManager.setDarkTheme(this, isDarkTheme.value)
                    }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        SessionManager.init(applicationContext)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "otp_channel",
                "OTP Verification",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for OTP notifications"
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

// ─── Navigation ──────────────────────────────────────────────────────────────
@Composable
fun AppNav(isDarkTheme: Boolean, onThemeToggle: () -> Unit) {
    var currentScreen by remember { mutableStateOf<String?>(null) }
    var selectedBookingForDetails by remember { mutableStateOf<BookingDetails?>(null) }
    var isNewBooking by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val colors = if (isDarkTheme) DarkTheme else LightTheme

    LaunchedEffect(Unit) {
        SessionManager.init(context)
        val isValidSession = SessionManager.isLoggedIn()
        currentScreen = if (isValidSession) "home" else "splash"
    }

    Scaffold(
        bottomBar = {
            if (currentScreen == "home" || currentScreen == "history") {
                GlobalBottomNavigation(
                    currentScreen = currentScreen,
                    onNavigate = { screen ->
                        when (screen) {
                            "home" -> currentScreen = "home"
                            "history" -> currentScreen = "history"
                            "logout" -> {
                                SessionManager.clearSession()
                                currentScreen = "login"
                            }
                        }
                    },
                    colors = colors
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (currentScreen) {
                "splash" -> ModernSplashScreen(
                    colors = colors,
                    onTimeout = { currentScreen = "login" }
                )
                "login" -> ModernLoginScreen(
                    colors = colors,
                    onLoginSuccess = { email ->
                        SessionManager.saveUserSession(email)
                        currentScreen = "home"
                    }
                )
                "home" -> ModernHomeScreen(
                    colors = colors,
                    isDarkTheme = isDarkTheme,
                    onThemeToggle = onThemeToggle,
                    onLogout = {
                        SessionManager.clearSession()
                        currentScreen = "login"
                    },
                    onBookClick = { templeName ->
                        currentScreen = "booking"
                    }
                )
                "booking" -> ModernBookingScreen(
                    colors = colors,
                    onBack = { currentScreen = "home" },
                    onBookingComplete = { details ->
                        SessionManager.saveBooking(details)
                        selectedBookingForDetails = details
                        isNewBooking = true
                        currentScreen = "confirmation"
                    }
                )
                "confirmation" -> {
                    val booking = selectedBookingForDetails
                    if (booking != null) {
                        ModernConfirmationScreen(
                            colors = colors,
                            bookingDetails = booking,
                            isNewBooking = isNewBooking,
                            onBack = {
                                selectedBookingForDetails = null
                                currentScreen = "home"
                            }
                        )
                    } else {
                        currentScreen = "home"
                    }
                }
                "history" -> ModernHistoryScreen(
                    colors = colors,
                    onBack = { currentScreen = "home" },
                    onBookingClick = { booking ->
                        selectedBookingForDetails = booking
                        isNewBooking = false
                        currentScreen = "confirmation"
                    }
                )
            }
        }
    }
}

@Composable
fun GlobalBottomNavigation(
    currentScreen: String?,
    onNavigate: (String) -> Unit,
    colors: ThemeColors
) {
    NavigationBar(
        containerColor = colors.surface.copy(alpha = 0.95f),
        tonalElevation = 0.dp,
        modifier = Modifier.shadow(10.dp)
    ) {
        val items = listOf(
            Triple("home", Icons.Default.Home, "Home"),
            Triple("history", Icons.Default.History, "History"),
            Triple("logout", Icons.Default.Logout, "Logout")
        )

        items.forEach { (screen, icon, title) ->
            NavigationBarItem(
                icon = {
                    Icon(
                        icon,
                        contentDescription = title,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text(title) },
                selected = currentScreen == screen,
                onClick = { onNavigate(screen) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colors.primary,
                    selectedTextColor = colors.primary,
                    unselectedIconColor = colors.textSecondary,
                    unselectedTextColor = colors.textSecondary
                )
            )
        }
    }
}

@Composable
fun AnimatedGradientBackground(colors: ThemeColors, modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier = modifier
            .background(colors.background)
            .drawBehind {
                val radius = size.minDimension / 2f
                val centerX = size.width / 2f
                val centerY = size.height / 2f

                for (i in 0..2) {
                    val angle = rotation + (i * 120f)
                    val x = centerX + radius * 0.7f * kotlin.math.cos(Math.toRadians(angle.toDouble())).toFloat()
                    val y = centerY + radius * 0.7f * kotlin.math.sin(Math.toRadians(angle.toDouble())).toFloat()

                    drawCircle(
                        color = when (i) {
                            0 -> colors.primary.copy(alpha = 0.05f)
                            1 -> colors.accent.copy(alpha = 0.05f)
                            else -> colors.primaryLight.copy(alpha = 0.05f)
                        },
                        radius = radius * 0.5f,
                        center = Offset(x, y)
                    )
                }
            }
    )
}

@Composable
fun GlassCard(
    colors: ThemeColors,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .shadow(20.dp, RoundedCornerShape(24.dp), spotColor = colors.primary.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.card.copy(alpha = 0.6f)
        ),
        border = BorderStroke(1.dp, Brush.linearGradient(listOf(Color.White.copy(alpha = 0.2f), Color.White.copy(alpha = 0.05f))))
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.08f),
                            Color.White.copy(alpha = 0.01f)
                        )
                    )
                )
                .padding(20.dp),
            content = content
        )
    }
}

@Composable
fun Floating3DButton(
    colors: ThemeColors,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector? = null,
    enabled: Boolean = true
) {
    var isPressed by remember { mutableStateOf(false) }

    Button(
        onClick = onClick,
        modifier = modifier
            .graphicsLayer {
                shadowElevation = if (isPressed) 10f else 0f
                scaleX = if (isPressed) 0.98f else 1f
                scaleY = if (isPressed) 0.98f else 1f
            },
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.primary,
            disabledContainerColor = colors.card
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(text, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

// ─── Modern Splash Screen ───────────────────────────────────────────────────────────
@Composable
fun ModernSplashScreen(colors: ThemeColors, onTimeout: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(3000)
        onTimeout()
    }

    val infiniteTransition = rememberInfiniteTransition()

    val logoRotationY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val logoScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val bgRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .drawBehind {
                val radius = size.minDimension
                val centerX = size.width / 2f
                val centerY = size.height / 2f
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(colors.primary.copy(alpha = 0.2f), Color.Transparent),
                        center = Offset(
                            centerX + radius * 0.4f * kotlin.math.cos(Math.toRadians(bgRotation.toDouble())).toFloat(),
                            centerY + radius * 0.4f * kotlin.math.sin(Math.toRadians(bgRotation.toDouble())).toFloat()
                        ),
                        radius = radius * 0.8f
                    )
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(colors.accent.copy(alpha = 0.15f), Color.Transparent),
                        center = Offset(
                            centerX - radius * 0.3f * kotlin.math.cos(Math.toRadians(bgRotation.toDouble())).toFloat(),
                            centerY - radius * 0.3f * kotlin.math.sin(Math.toRadians(bgRotation.toDouble())).toFloat()
                        ),
                        radius = radius * 0.6f
                    )
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .graphicsLayer {
                        rotationY = logoRotationY
                        scaleX = logoScale
                        scaleY = logoScale
                        cameraDistance = 16f * density
                    }
                    .background(
                        Brush.linearGradient(
                            colors = listOf(colors.primaryLight, colors.primary, colors.accent)
                        ),
                        CircleShape
                    )
                    .border(2.dp, Color.White.copy(alpha = 0.5f), CircleShape)
                    .shadow(40.dp, CircleShape, spotColor = colors.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🛕",
                    fontSize = 60.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "DarshanAI",
                style = TextStyle(
                    fontSize = 42.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp,
                    brush = Brush.linearGradient(
                        colors = listOf(Color.White, colors.primaryLight, colors.accent)
                    )
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                color = colors.card.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
            ) {
                Text(
                    text = "Smart Pilgrimage Experience",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.textSecondary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            CircularProgressIndicator(
                modifier = Modifier.size(40.dp),
                color = colors.primaryLight,
                strokeWidth = 3.dp
            )
        }
    }
}

// ─── Modern Login Screen ───────────────────────────────────────────────────────────
enum class LoginStep { EMAIL_INPUT, OTP_VERIFICATION }

@Composable
fun ModernLoginScreen(colors: ThemeColors, onLoginSuccess: (String) -> Unit) {
    var step by remember { mutableStateOf(LoginStep.EMAIL_INPUT) }
    var email by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var generatedOtp by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var countdown by remember { mutableStateOf(0) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(countdown) {
        if (countdown > 0) {
            delay(1000)
            countdown--
        }
    }

    AnimatedGradientBackground(colors)

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        GlassCard(
            colors = colors,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(colors.primary, colors.accent)
                                ),
                                CircleShape
                            )
                            .shadow(15.dp, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🛕", fontSize = 30.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (step == LoginStep.EMAIL_INPUT) "Welcome Back" else "Verify OTP",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.text
                    )

                    Text(
                        text = if (step == LoginStep.EMAIL_INPUT)
                            "Enter your email to continue"
                        else
                            "Enter the 6-digit code sent to your email",
                        fontSize = 13.sp,
                        color = colors.textSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            when (step) {
                LoginStep.EMAIL_INPUT -> {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it.lowercase().trim() },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Email Address") },
                        placeholder = { Text("your@email.com") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Email,
                                contentDescription = null,
                                tint = colors.primary
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        isError = errorMessage != null,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colors.primary,
                            unfocusedBorderColor = colors.card,
                            focusedContainerColor = colors.background,
                            unfocusedContainerColor = colors.background
                        )
                    )

                    if (errorMessage != null) {
                        Text(
                            text = errorMessage!!,
                            color = colors.warning,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Floating3DButton(
                        colors = colors,
                        onClick = {
                            if (validateEmail(email)) {
                                isLoading = true
                                errorMessage = null
                                generatedOtp = generateOTP()

                                coroutineScope.launch {
                                    Toast.makeText(context, "Sending OTP...", Toast.LENGTH_SHORT).show()

                                    val emailSent = withTimeoutOrNull(15000) {
                                        EmailService.sendOTPEmail(email, generatedOtp)
                                    } ?: false

                                    withContext(Dispatchers.Main) {
                                        isLoading = false
                                        if (emailSent) {
                                            step = LoginStep.OTP_VERIFICATION
                                            countdown = 60
                                            sendLocalOtpNotification(context, generatedOtp)
                                            Toast.makeText(context, "OTP sent successfully!", Toast.LENGTH_SHORT).show()
                                        } else {
                                            errorMessage = "Failed to send OTP. Please check your internet and try again."
                                        }
                                    }
                                }
                            } else {
                                errorMessage = "Please enter a valid email address"
                            }
                        },
                        text = "Send OTP",
                        enabled = email.isNotEmpty() && !isLoading
                    )
                }

                LoginStep.OTP_VERIFICATION -> {
                    Text(
                        text = "OTP sent to $email",
                        fontSize = 13.sp,
                        color = colors.textSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = otp,
                        onValueChange = {
                            if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                                otp = it
                                errorMessage = null
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Enter OTP") },
                        placeholder = { Text("123456") },
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        isError = errorMessage != null,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colors.primary,
                            unfocusedBorderColor = colors.card,
                            focusedContainerColor = colors.background,
                            unfocusedContainerColor = colors.background
                        )
                    )

                    if (errorMessage != null) {
                        Text(
                            text = errorMessage!!,
                            color = colors.warning,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Floating3DButton(
                        colors = colors,
                        onClick = {
                            if (otp == generatedOtp) {
                                onLoginSuccess(email)
                            } else {
                                errorMessage = "Invalid OTP. Please try again."
                            }
                        },
                        text = "Verify OTP",
                        enabled = otp.length == 6
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (countdown > 0) {
                            Text(
                                text = "Resend code in ${countdown}s",
                                fontSize = 13.sp,
                                color = colors.textSecondary
                            )
                        } else {
                            TextButton(
                                onClick = {
                                    coroutineScope.launch {
                                        isLoading = true
                                        generatedOtp = generateOTP()
                                        Toast.makeText(context, "Resending OTP...", Toast.LENGTH_SHORT).show()

                                        val emailSent = withTimeoutOrNull(15000) {
                                            EmailService.sendOTPEmail(email, generatedOtp)
                                        } ?: false

                                        withContext(Dispatchers.Main) {
                                            isLoading = false
                                            if (emailSent) {
                                                countdown = 60
                                                errorMessage = null
                                                sendLocalOtpNotification(context, generatedOtp)
                                                Toast.makeText(context, "OTP resent successfully!", Toast.LENGTH_SHORT).show()
                                            } else {
                                                errorMessage = "Failed to resend OTP."
                                            }
                                        }
                                    }
                                }
                            ) {
                                Text("Resend Code", color = colors.primary)
                            }
                        }
                    }

                    TextButton(
                        onClick = {
                            step = LoginStep.EMAIL_INPUT
                            otp = ""
                            errorMessage = null
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("← Change Email", color = colors.textSecondary)
                    }
                }
            }
        }
    }
}

// ─── Temple Detail Popup Dialog ───────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TempleDetailDialog(
    temple: Temple,
    colors: ThemeColors,
    onDismiss: () -> Unit
) {
    val scrollState = rememberScrollState()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.9f),
            shape = RoundedCornerShape(24.dp),
            color = colors.card,
            shadowElevation = 24.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(colors.primary.copy(alpha = 0.1f), colors.card)
                        )
                    )
            ) {
                // Header with gradient background
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(colors.primary, colors.accent)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column {
                                Text(
                                    text = temple.name,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = temple.location,
                                    fontSize = 14.sp,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            }
                            IconButton(
                                onClick = onDismiss,
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color.White.copy(alpha = 0.2f), CircleShape)
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Rating and crowd info
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color.White.copy(alpha = 0.2f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                        tint = Color.Yellow
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${temple.rating} ★",
                                        fontSize = 12.sp,
                                        color = Color.White
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color.White.copy(alpha = 0.2f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.People,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                        tint = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${temple.crowd}% crowded • ${temple.waitTime} wait",
                                        fontSize = 12.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }

                // Scrollable content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(scrollState)
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Quick Info Cards
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        QuickInfoCard(
                            colors = colors,
                            icon = Icons.Default.Schedule,
                            title = "Opening Hours",
                            value = temple.openingHours.split("\n").first()
                        )
                        QuickInfoCard(
                            colors = colors,
                            icon = Icons.Default.CalendarToday,
                            title = "Best Time",
                            value = temple.bestTimeToVisit.split("\n").first()
                        )
                    }

                    // Description Section
                    SectionCard(
                        colors = colors,
                        title = "📖 About the Temple",
                        icon = Icons.Default.Info
                    ) {
                        Text(
                            text = temple.fullDescription,
                            fontSize = 14.sp,
                            color = colors.text,
                            lineHeight = 22.sp
                        )
                    }

                    // Story Section
                    SectionCard(
                        colors = colors,
                        title = "📜 Legend & Story",
                        icon = Icons.Default.History
                    ) {
                        Text(
                            text = temple.story,
                            fontSize = 14.sp,
                            color = colors.text,
                            lineHeight = 22.sp
                        )
                    }

                    // Architecture Section
                    SectionCard(
                        colors = colors,
                        title = "🏛️ Architecture",
                        icon = Icons.Default.Business
                    ) {
                        Text(
                            text = temple.architecture,
                            fontSize = 14.sp,
                            color = colors.text,
                            lineHeight = 22.sp
                        )
                    }

                    // Location & Address
                    SectionCard(
                        colors = colors,
                        title = "📍 Location",
                        icon = Icons.Default.LocationOn
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = temple.address,
                                fontSize = 14.sp,
                                color = colors.text
                            )
                            Text(
                                text = "How to reach:",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colors.primary
                            )
                            Text(
                                text = temple.howToReach,
                                fontSize = 13.sp,
                                color = colors.textSecondary,
                                lineHeight = 20.sp
                            )
                        }
                    }

                    // Festivals
                    SectionCard(
                        colors = colors,
                        title = "🎉 Major Festivals",
                        icon = Icons.Default.Celebration
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            temple.festivals.forEach { festival ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "•",
                                        fontSize = 16.sp,
                                        color = colors.accent
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = festival,
                                        fontSize = 13.sp,
                                        color = colors.text
                                    )
                                }
                            }
                        }
                    }

                    // Nearby Attractions
                    SectionCard(
                        colors = colors,
                        title = "🏞️ Nearby Attractions",
                        icon = Icons.Default.Explore
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            temple.nearbyAttractions.forEach { attraction ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "📍",
                                        fontSize = 14.sp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = attraction,
                                        fontSize = 13.sp,
                                        color = colors.text
                                    )
                                }
                            }
                        }
                    }

                    // Visitor Tips
                    SectionCard(
                        colors = colors,
                        title = "💡 Visitor Tips",
                        icon = Icons.Default.Lightbulb
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            temple.tips.forEach { tip ->
                                Row(verticalAlignment = Alignment.Top) {
                                    Text(
                                        text = "✓",
                                        fontSize = 14.sp,
                                        color = colors.success
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = tip,
                                        fontSize = 13.sp,
                                        color = colors.text,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Bottom Button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Close", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun QuickInfoCard(colors: ThemeColors, icon: ImageVector, title: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface.copy(alpha = 0.8f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, tint = colors.primary, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(title, fontSize = 11.sp, color = colors.textSecondary, textAlign = TextAlign.Center)
            Text(value, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = colors.text, maxLines = 2, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun SectionCard(
    colors: ThemeColors,
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface.copy(alpha = 0.6f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = colors.primary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = colors.primary)
            }
            content()
        }
    }
}

// ─── Modern Home Screen with Temple Details ───────────────────────────────────────────────────────────
@Composable
fun ModernHomeScreen(
    colors: ThemeColors,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    onLogout: () -> Unit,
    onBookClick: (String) -> Unit
) {
    var showSessionDialog by remember { mutableStateOf(false) }
    var remainingTime by remember { mutableStateOf(SessionManager.getFormattedRemainingTime()) }
    var selectedTempleForDetails by remember { mutableStateOf<Temple?>(null) }

    val temples = TempleDataManager.getAllTemples()
    var selectedTemple by remember { mutableStateOf(temples[0]) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(60000)
            remainingTime = SessionManager.getFormattedRemainingTime()
        }
    }

    ModernHomeContent(
        colors = colors,
        temples = temples,
        selectedTemple = selectedTemple,
        onTempleSelected = { selectedTemple = it },
        onBookClick = { onBookClick(selectedTemple.name) },
        onThemeToggle = onThemeToggle,
        isDarkTheme = isDarkTheme,
        onTempleDetailsClick = { temple -> selectedTempleForDetails = temple }
    )

    // Show temple details dialog when a temple is selected
    selectedTempleForDetails?.let { temple ->
        TempleDetailDialog(
            temple = temple,
            colors = colors,
            onDismiss = { selectedTempleForDetails = null }
        )
    }

    if (showSessionDialog) {
        AlertDialog(
            onDismissRequest = { showSessionDialog = false },
            title = {
                Text(
                    "Session Information",
                    color = colors.text,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        "You are logged in as:",
                        color = colors.textSecondary,
                        fontSize = 13.sp
                    )
                    Text(
                        SessionManager.getUser() ?: "Unknown",
                        color = colors.primary,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = colors.card)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Session expires in:",
                        color = colors.textSecondary,
                        fontSize = 13.sp
                    )
                    Text(
                        remainingTime,
                        color = if (SessionManager.getSessionRemainingTime() > TimeUnit.HOURS.toMillis(1))
                            colors.success else colors.warning,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Session duration: 24 hours",
                        fontSize = 11.sp,
                        color = colors.textSecondary
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showSessionDialog = false }
                ) {
                    Text("OK", color = colors.primary)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onLogout()
                        showSessionDialog = false
                    }
                ) {
                    Text("Logout", color = colors.warning)
                }
            },
            containerColor = colors.card,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
fun ModernHomeContent(
    colors: ThemeColors,
    temples: List<Temple>,
    selectedTemple: Temple,
    onTempleSelected: (Temple) -> Unit,
    onBookClick: () -> Unit,
    onThemeToggle: () -> Unit,
    isDarkTheme: Boolean,
    onTempleDetailsClick: (Temple) -> Unit
) {
    val userInitial = SessionManager.getUserInitial()
    val infiniteTransition = rememberInfiniteTransition()
    val floatY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(colors.primary.copy(alpha = 0.3f), colors.background)
                    )
                )
                .padding(top = 48.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Namaste, ${userInitial}! 🙏",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.text
                    )
                    Text(
                        text = "Find your perfect darshan slot",
                        fontSize = 14.sp,
                        color = colors.textSecondary
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    IconButton(
                        onClick = onThemeToggle,
                        modifier = Modifier
                            .size(50.dp)
                            .background(colors.card, CircleShape)
                    ) {
                        Icon(
                            if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle Theme",
                            tint = colors.primary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(colors.primary, colors.accent)
                                ),
                                CircleShape
                            )
                            .shadow(10.dp, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userInitial,
                            fontSize = 20.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Featured Temple Card - Updates when selectedTemple changes
        GlassCard(
            colors = colors,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(colors.primary, colors.accent)
                        )
                    )
            ) {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .offset(x = (-30).dp, y = (-30).dp)
                        .background(
                            Color.White.copy(alpha = 0.1f),
                            CircleShape
                        )
                )
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .offset(x = 100.dp, y = 50.dp)
                        .background(
                            Color.White.copy(alpha = 0.05f),
                            CircleShape
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "⭐ FEATURED",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f),
                            letterSpacing = 2.sp
                        )
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "${selectedTemple.rating} ★",
                                fontSize = 12.sp,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Text(
                        text = selectedTemple.name,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = selectedTemple.description,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.8f),
                        maxLines = 2
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatPill(
                    colors = colors,
                    icon = Icons.Default.People,
                    label = "Crowd",
                    value = "${selectedTemple.crowd}%",
                    color = if (selectedTemple.crowd > 70) colors.warning else colors.success
                )
                StatPill(
                    colors = colors,
                    icon = Icons.Default.AccessTime,
                    label = "Wait Time",
                    value = selectedTemple.waitTime,
                    color = colors.primary
                )
                StatPill(
                    colors = colors,
                    icon = Icons.Default.Star,
                    label = "Rating",
                    value = "${selectedTemple.rating}",
                    color = colors.accent
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onBookClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Bookmark, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Book Darshan →", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                Button(
                    onClick = { onTempleDetailsClick(selectedTemple) },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary.copy(alpha = 0.8f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Details", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        Text(
            text = "Popular Temples",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = colors.text,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(temples) { temple ->
                ModernTempleCard(
                    colors = colors,
                    temple = temple,
                    isSelected = selectedTemple.name == temple.name,
                    onClick = { onTempleSelected(temple) },
                    onDetailsClick = { onTempleDetailsClick(temple) }
                )
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun StatPill(colors: ThemeColors, icon: ImageVector, label: String, value: String, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(
            colors.card.copy(alpha = 0.5f),
            RoundedCornerShape(12.dp)
        ).padding(12.dp)
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, fontSize = 11.sp, color = colors.textSecondary)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
fun ModernTempleCard(
    colors: ThemeColors,
    temple: Temple,
    isSelected: Boolean,
    onClick: () -> Unit,
    onDetailsClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Card(
        modifier = Modifier
            .width(160.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) colors.primary.copy(alpha = 0.2f) else colors.card
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(colors.primary, colors.accent)
                        ),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("🛕", fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                temple.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isSelected) colors.primaryLight else colors.text
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    Icons.Default.AccessTime,
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = colors.textSecondary
                )
                Text(temple.waitTime, fontSize = 11.sp, color = colors.textSecondary)
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = temple.crowd / 100f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = if (temple.crowd > 70) colors.warning else colors.success,
                trackColor = colors.card
            )

            Text(
                "${temple.crowd}% crowded",
                fontSize = 10.sp,
                color = colors.textSecondary,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onDetailsClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary.copy(alpha = 0.8f)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Details", fontSize = 11.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

// ─── Modern Booking Screen ───────────────────────────────────────────────────────────
@Composable
fun ModernBookingScreen(colors: ThemeColors, onBack: () -> Unit, onBookingComplete: (BookingDetails) -> Unit) {
    data class DateOption(
        val day: String,
        val date: String,
        val month: String,
        val fullDate: String,
        val timestamp: Long
    )

    val dateOptions = remember {
        val calendar = java.util.Calendar.getInstance()
        val dateFormat = java.text.SimpleDateFormat("dd", java.util.Locale.getDefault())
        val monthFormat = java.text.SimpleDateFormat("MMM", java.util.Locale.getDefault())
        val dayFormat = java.text.SimpleDateFormat("EEE", java.util.Locale.getDefault())
        val fullDateFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())

        val options = mutableListOf<DateOption>()
        for (i in 0..6) {
            val currentDate = calendar.time
            options.add(
                DateOption(
                    day = dayFormat.format(currentDate),
                    date = dateFormat.format(currentDate),
                    month = monthFormat.format(currentDate),
                    fullDate = fullDateFormat.format(currentDate),
                    timestamp = currentDate.time
                )
            )
            calendar.add(java.util.Calendar.DAY_OF_YEAR, 1)
        }
        options
    }

    val timeSlots = listOf(
        "6:00 – 8:00 AM",
        "8:00 – 10:00 AM",
        "10:00 – 12:00 PM",
        "4:00 – 6:00 PM",
        "6:00 – 8:00 PM"
    )

    var selectedDateIndex by remember { mutableStateOf(0) }
    var selectedTimeSlot by remember { mutableStateOf("") }
    var peopleCount by remember { mutableStateOf(1) }
    var showYatriForm by remember { mutableStateOf(false) }
    var Yatris by remember { mutableStateOf<List<Yatri>>(emptyList()) }
    var currentYatriIndex by remember { mutableStateOf(0) }

    var currentName by remember { mutableStateOf("") }
    var currentAge by remember { mutableStateOf("") }
    var currentGender by remember { mutableStateOf("") }

    AnimatedGradientBackground(colors)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.background)
                .padding(top = 48.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(40.dp)
                        .background(colors.card, CircleShape)
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = colors.text,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Book Darshan Slot",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.text
                    )
                    Text(
                        text = "Complete your booking details",
                        fontSize = 13.sp,
                        color = colors.textSecondary
                    )
                }
            }
        }

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            if (!showYatriForm) {
                GlassCard(
                    colors = colors,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Select Date",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colors.text
                            )
                            Text(
                                text = "Showing next 7 days",
                                fontSize = 11.sp,
                                color = colors.textSecondary
                            )
                        }

                        if (selectedDateIndex == 0) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = colors.primary.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "Today",
                                    fontSize = 10.sp,
                                    color = colors.primary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(dateOptions.size) { index ->
                            val date = dateOptions[index]
                            val isSelected = selectedDateIndex == index
                            val isToday = index == 0
                            val isPast = date.timestamp < System.currentTimeMillis() && !isToday

                            Box(
                                modifier = Modifier
                                    .width(75.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        when {
                                            isSelected -> colors.primary
                                            isPast -> colors.card.copy(alpha = 0.5f)
                                            else -> colors.card
                                        }
                                    )
                                    .border(
                                        width = if (isSelected) 0.dp else 1.dp,
                                        color = colors.card,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .let { modifier ->
                                        if (!isPast) {
                                            modifier.clickable {
                                                if (!isPast) selectedDateIndex = index
                                            }
                                        } else modifier
                                    }
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = date.day,
                                        fontSize = 12.sp,
                                        color = when {
                                            isSelected -> Color.White
                                            isPast -> colors.textSecondary.copy(alpha = 0.5f)
                                            else -> colors.textSecondary
                                        }
                                    )
                                    Text(
                                        text = date.date,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = when {
                                            isSelected -> Color.White
                                            isPast -> colors.text.copy(alpha = 0.5f)
                                            else -> colors.text
                                        }
                                    )
                                    Text(
                                        text = date.month,
                                        fontSize = 11.sp,
                                        color = when {
                                            isSelected -> Color.White.copy(alpha = 0.8f)
                                            isPast -> colors.textSecondary.copy(alpha = 0.5f)
                                            else -> colors.textSecondary
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                GlassCard(
                    colors = colors,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Select Time Slot",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.text
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        timeSlots.chunked(2).forEach { rowSlots ->
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                rowSlots.forEach { slot ->
                                    val isSelected = selectedTimeSlot == slot
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(
                                                if (isSelected) colors.primary else colors.card
                                            )
                                            .clickable { selectedTimeSlot = slot }
                                            .padding(vertical = 12.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            slot,
                                            fontSize = 13.sp,
                                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                            color = if (isSelected) Color.White else colors.text
                                        )
                                    }
                                }
                                if (rowSlots.size == 1) Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                GlassCard(
                    colors = colors,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "Number of People",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colors.text
                            )
                            Text(
                                "Maximum 10 per slot",
                                fontSize = 11.sp,
                                color = colors.textSecondary
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            IconButton(
                                onClick = { if (peopleCount > 1) peopleCount-- },
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(colors.card, CircleShape)
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = null, tint = colors.text)
                            }

                            Text(
                                text = peopleCount.toString(),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.text
                            )

                            IconButton(
                                onClick = { if (peopleCount < 10) peopleCount++ },
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(colors.primary, CircleShape)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (selectedTimeSlot.isEmpty()) {
                    Text(
                        text = "Please select a time slot to continue",
                        fontSize = 12.sp,
                        color = colors.warning,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Floating3DButton(
                    colors = colors,
                    onClick = {
                        if (selectedTimeSlot.isNotEmpty()) {
                            showYatriForm = true
                            Yatris = List(peopleCount) { index ->
                                Yatri(index, "", "", "")
                            }
                        }
                    },
                    text = "Continue to Yatri Details →",
                    enabled = selectedTimeSlot.isNotEmpty()
                )
            } else {
                GlassCard(
                    colors = colors,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Yatri ${currentYatriIndex + 1} of ${Yatris.size}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.text
                        )

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(colors.primary.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("👤", fontSize = 20.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                GlassCard(
                    colors = colors,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = currentName,
                        onValueChange = { currentName = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Full Name") },
                        placeholder = { Text("Enter Yatri name") },
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colors.primary,
                            unfocusedBorderColor = colors.card,
                            focusedContainerColor = colors.background,
                            unfocusedContainerColor = colors.background
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = currentAge,
                        onValueChange = {
                            if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                                currentAge = it
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Age") },
                        placeholder = { Text("Enter age") },
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colors.primary,
                            unfocusedBorderColor = colors.card,
                            focusedContainerColor = colors.background,
                            unfocusedContainerColor = colors.background
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Gender",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = colors.textSecondary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf("Male", "Female", "Other").forEach { gender ->
                            FilterChip(
                                selected = currentGender == gender,
                                onClick = { currentGender = gender },
                                label = { Text(gender, fontSize = 13.sp) },
                                modifier = Modifier.weight(1f),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = colors.primary.copy(alpha = 0.2f),
                                    selectedLabelColor = colors.primary,
                                    disabledSelectedContainerColor = colors.card,
                                    containerColor = colors.card
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (currentYatriIndex > 0) {
                        OutlinedButton(
                            onClick = {
                                val updatedYatris = Yatris.toMutableList()
                                updatedYatris[currentYatriIndex] = Yatri(
                                    currentYatriIndex, currentName, currentAge, currentGender
                                )
                                Yatris = updatedYatris

                                currentYatriIndex--
                                val prevYatri = Yatris[currentYatriIndex]
                                currentName = prevYatri.name
                                currentAge = prevYatri.age
                                currentGender = prevYatri.gender
                            },
                            modifier = Modifier.weight(1f).height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = colors.text
                            ),
                            border = BorderStroke(1.dp, colors.card)
                        ) {
                            Text("← Previous")
                        }
                    }

                    Floating3DButton(
                        colors = colors,
                        onClick = {
                            if (currentName.isNotBlank() && currentAge.isNotBlank() && currentGender.isNotBlank()) {
                                val updatedYatris = Yatris.toMutableList()
                                updatedYatris[currentYatriIndex] = Yatri(
                                    currentYatriIndex, currentName, currentAge, currentGender
                                )
                                Yatris = updatedYatris

                                if (currentYatriIndex < Yatris.size - 1) {
                                    currentYatriIndex++
                                    val nextYatri = Yatris[currentYatriIndex]
                                    currentName = nextYatri.name
                                    currentAge = nextYatri.age
                                    currentGender = nextYatri.gender
                                } else {
                                    val selectedDate = dateOptions[selectedDateIndex]
                                    val bookingDetails = BookingDetails(
                                        templeName = "Somnath Temple",
                                        date = "${selectedDate.date} ${selectedDate.month}, ${java.text.SimpleDateFormat("yyyy", java.util.Locale.getDefault()).format(java.util.Date(selectedDate.timestamp))}",
                                        timeSlot = selectedTimeSlot,
                                        Yatris = Yatris,
                                        bookingId = generateBookingId()
                                    )
                                    onBookingComplete(bookingDetails)
                                }
                            }
                        },
                        text = if (currentYatriIndex < Yatris.size - 1) "Next →" else "Complete Booking ✓",
                        enabled = currentName.isNotBlank() && currentAge.isNotBlank() && currentGender.isNotBlank(),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

// ─── Modern Confirmation Screen ───────────────────────────────────────────────────────────
sealed class EmailStatus {
    object Idle : EmailStatus()
    object Sending : EmailStatus()
    object Sent : EmailStatus()
    object Failed : EmailStatus()
}

@Composable
fun ModernConfirmationScreen(colors: ThemeColors, bookingDetails: BookingDetails, isNewBooking: Boolean, onBack: () -> Unit) {
    var selectedYatriIndex by remember { mutableStateOf(0) }
    var emailStatus by remember { mutableStateOf<EmailStatus>(EmailStatus.Idle) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userEmail = SessionManager.getUser() ?: ""

    val qrBitmap = remember(bookingDetails.bookingId, selectedYatriIndex) {
        val qrData = buildString {
            append("BOOKING_ID:${bookingDetails.bookingId}\n")
            append("TEMPLE:${bookingDetails.templeName}\n")
            append("DATE:${bookingDetails.date}\n")
            append("TIME:${bookingDetails.timeSlot}\n")
            append("Yatri:${bookingDetails.Yatris[selectedYatriIndex].name}\n")
            append("AGE:${bookingDetails.Yatris[selectedYatriIndex].age}\n")
            append("GENDER:${bookingDetails.Yatris[selectedYatriIndex].gender}")
        }
        QRCodeGenerator.generateQRBitmap(qrData)
    }

    LaunchedEffect(Unit) {
        if (isNewBooking && emailStatus == EmailStatus.Idle && userEmail.isNotEmpty()) {
            emailStatus = EmailStatus.Sending
            Toast.makeText(context, "Booking confirmed!", Toast.LENGTH_SHORT).show()

            coroutineScope.launch {
                val success = withTimeoutOrNull(20000) {
                    EmailService.sendBookingConfirmation(userEmail, bookingDetails, qrBitmap)
                } ?: false

                withContext(Dispatchers.Main) {
                    emailStatus = if (success) EmailStatus.Sent else EmailStatus.Failed
                    if (!success) {
                        Toast.makeText(context,
                            "Booking saved! Email will be sent shortly.",
                            Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    AnimatedGradientBackground(colors)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.background)
                .padding(top = 48.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(40.dp)
                        .background(colors.card, CircleShape)
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = colors.text,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Booking Confirmed! 🎉",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.text
                    )
                    when (emailStatus) {
                        EmailStatus.Sending -> {
                            Text(
                                text = "📧 Sending confirmation email...",
                                fontSize = 12.sp,
                                color = colors.textSecondary
                            )
                        }
                        EmailStatus.Sent -> {
                            Text(
                                text = "✓ Confirmation email sent!",
                                fontSize = 12.sp,
                                color = colors.success
                            )
                        }
                        EmailStatus.Failed -> {
                            Text(
                                text = "⚠️ Email will be resent automatically",
                                fontSize = 12.sp,
                                color = colors.warning
                            )
                        }
                        else -> {}
                    }
                }
            }
        }

        if (bookingDetails.Yatris.size > 1) {
            LazyRow(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(bookingDetails.Yatris.size) { index ->
                    val yatri = bookingDetails.Yatris[index]
                    val isSelected = selectedYatriIndex == index

                    Surface(
                        modifier = Modifier
                            .clickable { selectedYatriIndex = index }
                            .graphicsLayer {
                                scaleX = if (isSelected) 1.05f else 1f
                                scaleY = if (isSelected) 1.05f else 1f
                            },
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) colors.primary else colors.card
                    ) {
                        Text(
                            yatri.name.take(15),
                            fontSize = 13.sp,
                            color = if (isSelected) Color.White else colors.text,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val currentYatri = bookingDetails.Yatris[selectedYatriIndex]

            GlassCard(
                colors = colors,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    val infiniteTransition = rememberInfiniteTransition()
                    val scanAnimation by infiniteTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(2000, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse
                        )
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(200.dp)
                                .shadow(20.dp, RoundedCornerShape(16.dp))
                                .background(Color.White, RoundedCornerShape(16.dp))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                bitmap = qrBitmap.asImageBitmap(),
                                contentDescription = "QR Code",
                                modifier = Modifier.fillMaxSize()
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(3.dp)
                                    .background(
                                        Brush.horizontalGradient(
                                            colors = listOf(Color.Transparent, colors.primary, colors.accent, Color.Transparent)
                                        )
                                    )
                                    .offset(y = (scanAnimation * 180).dp)
                            )
                        }

                        Text(
                            text = "📸 Show this QR at temple entrance",
                            fontSize = 12.sp,
                            color = colors.textSecondary
                        )
                    }
                }
            }

            GlassCard(
                colors = colors,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Booking Details",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.text
                )

                Spacer(modifier = Modifier.height(16.dp))

                DetailRowModern(colors, "Booking ID", bookingDetails.bookingId)
                Divider(color = colors.card, modifier = Modifier.padding(vertical = 8.dp))
                DetailRowModern(colors, "Yatri Name", currentYatri.name)
                DetailRowModern(colors, "Age", "${currentYatri.age} years")
                DetailRowModern(colors, "Gender", currentYatri.gender)
                Divider(color = colors.card, modifier = Modifier.padding(vertical = 8.dp))
                DetailRowModern(colors, "Temple", bookingDetails.templeName)
                DetailRowModern(colors, "Date", bookingDetails.date)
                DetailRowModern(colors, "Time Slot", bookingDetails.timeSlot)

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.success.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = colors.success,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when (emailStatus) {
                            EmailStatus.Sent -> "Booking Confirmed & Email Sent!"
                            EmailStatus.Sending -> "Booking Confirmed - Sending Email..."
                            else -> "Booking Confirmed!"
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = when (emailStatus) {
                            EmailStatus.Sent -> colors.success
                            else -> colors.text
                        }
                    )
                }
            }

            Floating3DButton(
                colors = colors,
                onClick = onBack,
                text = "Back to Home",
                icon = Icons.Default.Home
            )
        }
    }
}

@Composable
fun DetailRowModern(colors: ThemeColors, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = colors.textSecondary
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = colors.text
        )
    }
}

// ─── Modern History Screen ───────────────────────────────────────────────────────────
@Composable
fun ModernHistoryScreen(colors: ThemeColors, onBack: () -> Unit, onBookingClick: (BookingDetails) -> Unit) {
    val history = SessionManager.getBookingHistory()

    AnimatedGradientBackground(colors)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.background)
                .padding(top = 48.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(40.dp)
                        .background(colors.card, CircleShape)
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = colors.text,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Booking History",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.text
                    )
                    Text(
                        text = "${history.size} total bookings",
                        fontSize = 13.sp,
                        color = colors.textSecondary
                    )
                }
            }
        }

        if (history.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        Icons.Default.History,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = colors.textSecondary
                    )
                    Text(
                        text = "No Bookings Yet",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = colors.text
                    )
                    Text(
                        text = "Your booking history will appear here",
                        fontSize = 14.sp,
                        color = colors.textSecondary
                    )
                    Floating3DButton(
                        colors = colors,
                        onClick = onBack,
                        text = "Book Your First Darshan →",
                        modifier = Modifier.width(200.dp)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(history) { booking ->
                    ModernHistoryCard(
                        colors = colors,
                        booking = booking,
                        onClick = { onBookingClick(booking) }
                    )
                }
            }
        }
    }
}

@Composable
fun ModernHistoryCard(colors: ThemeColors, booking: BookingDetails, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .shadow(10.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.card)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(colors.primary.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🛕", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = booking.templeName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.text
                        )
                        Text(
                            text = booking.bookingId.take(8),
                            fontSize = 11.sp,
                            color = colors.textSecondary
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = colors.success.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = "Confirmed",
                        fontSize = 11.sp,
                        color = colors.success,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                HistoryDetailChip(colors, Icons.Default.DateRange, booking.date)
                HistoryDetailChip(colors, Icons.Default.AccessTime, booking.timeSlot)
            }

            Divider(color = colors.surface, modifier = Modifier.padding(vertical = 4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${booking.Yatris.size} Yatri(s)",
                    fontSize = 12.sp,
                    color = colors.textSecondary
                )
                Text(
                    text = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())
                        .format(java.util.Date(booking.timestamp)),
                    fontSize = 11.sp,
                    color = colors.textSecondary
                )
            }
        }
    }
}

@Composable
fun HistoryDetailChip(colors: ThemeColors, icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.background(colors.surface, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(12.dp), tint = colors.textSecondary)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, fontSize = 11.sp, color = colors.textSecondary)
    }
}

// ─── Helper Functions ───────────────────────────────────────────────────────────
fun validateEmail(email: String): Boolean {
    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@(.+)$")
    return email.isNotEmpty() && emailRegex.matches(email)
}

fun generateOTP(): String {
    return (100000..999999).random().toString()
}

fun sendLocalOtpNotification(context: Context, otp: String) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val notification = NotificationCompat.Builder(context, "otp_channel")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("DarshanAI - OTP Sent")
        .setContentText("OTP: $otp has been sent to your email")
        .setStyle(NotificationCompat.BigTextStyle()
            .bigText("Your OTP is: $otp\nCheck your email inbox (or spam folder) for the OTP.\nThis OTP is valid for 60 seconds!"))
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .build()

    notificationManager.notify(Random.nextInt(), notification)
}

fun generateBookingId(): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    return (1..8).map { chars[Random.nextInt(chars.length)] }.joinToString("")
}
