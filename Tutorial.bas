Type=Activity
Version=6.5
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: false
#End Region
Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.

End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	Dim wb As WebView
	Dim wv As WebViewSettings
	Dim tlb,lb As Label
	Dim Banner As AdView
End Sub

Sub Activity_Create(FirstTime As Boolean)
	wb.Initialize("wb")
	Activity.AddView(wb,0%x,55dip,100%x,100%y)
	wb.LoadUrl("http://www.htetznaing.com/myanmar-font-styles/")
	wv.setDisplayZoomControls(wb , False)
	
	tlb.Initialize("tlb")
	tlb.Text = "Tutorial"
	tlb.Color = Colors.RGB(33,150,243)
	tlb.TextColor = Colors.White
	tlb.TextSize = 25
	tlb.Typeface = Typeface.DEFAULT_BOLD
	
	tlb.Gravity = Gravity.CENTER
	Activity.AddView(tlb,0%x,0%y,100%x,55dip)
	
	Banner.Initialize("Banner","ca-app-pub-4173348573252986/1553645756")
	Banner.LoadAd
	Activity.AddView(Banner,0%x,100%y - 50dip,100%x,50dip)
	
	lb.Initialize("lb")
	lb.SetBackgroundImage(LoadBitmap(File.DirAssets,"open.png"))
	Activity.AddView(lb,100%x - 40dip,12.5dip,30dip,30dip)
End Sub

Sub lb_Click
	Dim p As PhoneIntents
	StartActivity(p.OpenBrowser("http://www.htetznaing.com/myanmar-font-styles/"))
End Sub
Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub