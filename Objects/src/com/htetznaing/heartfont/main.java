package com.htetznaing.heartfont;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = true;
	public static final boolean includeTitle = false;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			processBA = new BA(this.getApplicationContext(), null, null, "com.htetznaing.heartfont", "com.htetznaing.heartfont.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
				p.finish();
			}
		}
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		mostCurrent = this;
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(processBA, wl, false))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "com.htetznaing.heartfont", "com.htetznaing.heartfont.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "com.htetznaing.heartfont.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEvent(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return main.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null) //workaround for emulator bug (Issue 2423)
            return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
			if (mostCurrent == null || mostCurrent != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
		    processBA.raiseEvent(mostCurrent._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        Object[] o;
        if (permissions.length > 0)
            o = new Object[] {permissions[0], grantResults[0] == 0};
        else
            o = new Object[] {"", false};
        processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.objects.Timer _t = null;
public static anywheresoftware.b4a.objects.Timer _t1 = null;
public static anywheresoftware.b4a.objects.Timer _t2 = null;
public static anywheresoftware.b4a.objects.Timer _t3 = null;
public static anywheresoftware.b4a.objects.Timer _ad1 = null;
public static anywheresoftware.b4a.objects.Timer _ad2 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imv = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imv1 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imv2 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imv3 = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _iv1 = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _iv2 = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _iv3 = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _iv4 = null;
public giuseppe.salvi.icos.library.ICOSSlideAnimation _ani = null;
public anywheresoftware.b4a.phone.Phone _ph = null;
public com.htoophyoe.anitext.animatetext _atx = null;
public anywheresoftware.b4a.objects.ButtonWrapper _b1 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _b2 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _b3 = null;
public anywheresoftware.b4a.keywords.constants.TypefaceWrapper _mm = null;
public anywheresoftware.b4a.objects.LabelWrapper _lb = null;
public anywheresoftware.b4a.objects.LabelWrapper _lfoot = null;
public anywheresoftware.b4a.admobwrapper.AdViewWrapper _banner = null;
public mobi.mindware.admob.interstitial.AdmobInterstitialsAds _interstitial = null;
public com.htetznaing.heartfont.starter _starter = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
anywheresoftware.b4a.objects.drawable.ColorDrawable _b1bg = null;
 //BA.debugLineNum = 34;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 35;BA.debugLine="If ph.SdkVersion > 19 Then";
if (mostCurrent._ph.getSdkVersion()>19) { 
 //BA.debugLineNum = 36;BA.debugLine="Banner.Initialize(\"Banner\",\"ca-app-pub-417334857";
mostCurrent._banner.Initialize(mostCurrent.activityBA,"Banner","ca-app-pub-4173348573252986/5809490158");
 //BA.debugLineNum = 37;BA.debugLine="Banner.LoadAd";
mostCurrent._banner.LoadAd();
 //BA.debugLineNum = 38;BA.debugLine="Activity.AddView(Banner,0%x,80%y,100%x,50dip)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._banner.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (80),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 40;BA.debugLine="Interstitial.Initialize(\"Interstitial\",\"ca-app-p";
mostCurrent._interstitial.Initialize(mostCurrent.activityBA,"Interstitial","ca-app-pub-4173348573252986/8762956556");
 //BA.debugLineNum = 41;BA.debugLine="Interstitial.LoadAd";
mostCurrent._interstitial.LoadAd(mostCurrent.activityBA);
 //BA.debugLineNum = 43;BA.debugLine="ad1.Initialize(\"ad1\",100)";
_ad1.Initialize(processBA,"ad1",(long) (100));
 //BA.debugLineNum = 44;BA.debugLine="ad1.Enabled = False";
_ad1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 45;BA.debugLine="ad2.Initialize(\"ad2\",30000)";
_ad2.Initialize(processBA,"ad2",(long) (30000));
 //BA.debugLineNum = 46;BA.debugLine="ad2.Enabled = True";
_ad2.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 48;BA.debugLine="Activity.Color = Colors.White";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 49;BA.debugLine="ph.SetScreenOrientation(1)";
mostCurrent._ph.SetScreenOrientation(processBA,(int) (1));
 //BA.debugLineNum = 50;BA.debugLine="ani.SlideFadeFromRight(\"ani\",600,2000)";
mostCurrent._ani.SlideFadeFromRight(mostCurrent.activityBA,"ani",(float) (600),(long) (2000));
 //BA.debugLineNum = 51;BA.debugLine="iv1.Initialize(LoadBitmap(File.DirAssets,\"1.png\"))";
mostCurrent._iv1.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"1.png").getObject()));
 //BA.debugLineNum = 52;BA.debugLine="iv2.Initialize(LoadBitmap(File.DirAssets,\"2.png\"))";
mostCurrent._iv2.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"2.png").getObject()));
 //BA.debugLineNum = 53;BA.debugLine="iv3.Initialize(LoadBitmap(File.DirAssets,\"3.png\"))";
mostCurrent._iv3.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"3.png").getObject()));
 //BA.debugLineNum = 54;BA.debugLine="iv4.Initialize(LoadBitmap(File.DirAssets,\"4.png\"))";
mostCurrent._iv4.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"4.png").getObject()));
 //BA.debugLineNum = 56;BA.debugLine="t.Initialize(\"t\",7000)";
_t.Initialize(processBA,"t",(long) (7000));
 //BA.debugLineNum = 57;BA.debugLine="t1.Initialize(\"t1\",7000)";
_t1.Initialize(processBA,"t1",(long) (7000));
 //BA.debugLineNum = 58;BA.debugLine="t2.Initialize(\"t2\",7000)";
_t2.Initialize(processBA,"t2",(long) (7000));
 //BA.debugLineNum = 59;BA.debugLine="t3.Initialize(\"t3\",7000)";
_t3.Initialize(processBA,"t3",(long) (7000));
 //BA.debugLineNum = 60;BA.debugLine="t1.Enabled = True";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 61;BA.debugLine="t2.Enabled = False";
_t2.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 62;BA.debugLine="t3.Enabled = False";
_t3.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 64;BA.debugLine="imv.Initialize(\"imv\")";
mostCurrent._imv.Initialize(mostCurrent.activityBA,"imv");
 //BA.debugLineNum = 65;BA.debugLine="imv.Background = iv1";
mostCurrent._imv.setBackground((android.graphics.drawable.Drawable)(mostCurrent._iv1.getObject()));
 //BA.debugLineNum = 66;BA.debugLine="Activity.AddView(imv,0%x,0%x,100%x,170dip)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._imv.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (170)));
 //BA.debugLineNum = 68;BA.debugLine="imv1.Initialize(\"imv1\")";
mostCurrent._imv1.Initialize(mostCurrent.activityBA,"imv1");
 //BA.debugLineNum = 69;BA.debugLine="imv1.Background = iv2";
mostCurrent._imv1.setBackground((android.graphics.drawable.Drawable)(mostCurrent._iv2.getObject()));
 //BA.debugLineNum = 71;BA.debugLine="imv2.Initialize(\"imv2\")";
mostCurrent._imv2.Initialize(mostCurrent.activityBA,"imv2");
 //BA.debugLineNum = 72;BA.debugLine="imv2.Background = iv3";
mostCurrent._imv2.setBackground((android.graphics.drawable.Drawable)(mostCurrent._iv3.getObject()));
 //BA.debugLineNum = 74;BA.debugLine="imv3.Initialize(\"imv3\")";
mostCurrent._imv3.Initialize(mostCurrent.activityBA,"imv3");
 //BA.debugLineNum = 75;BA.debugLine="imv3.Background = iv4";
mostCurrent._imv3.setBackground((android.graphics.drawable.Drawable)(mostCurrent._iv4.getObject()));
 //BA.debugLineNum = 77;BA.debugLine="lb.Initialize(\"\")";
mostCurrent._lb.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 78;BA.debugLine="lb.Typeface = mm";
mostCurrent._lb.setTypeface((android.graphics.Typeface)(mostCurrent._mm.getObject()));
 //BA.debugLineNum = 79;BA.debugLine="lb.TextColor = Colors.Black";
mostCurrent._lb.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 80;BA.debugLine="Activity.AddView(lb,0%x,(imv.Top+imv.Height)+3%y,1";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lb.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),(int) ((mostCurrent._imv.getTop()+mostCurrent._imv.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (5),mostCurrent.activityBA));
 //BA.debugLineNum = 81;BA.debugLine="lb.Gravity = Gravity.CENTER";
mostCurrent._lb.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 82;BA.debugLine="lb.TextSize = 17";
mostCurrent._lb.setTextSize((float) (17));
 //BA.debugLineNum = 83;BA.debugLine="atx.initialize(\"atx\",Me,150)";
mostCurrent._atx._initialize(mostCurrent.activityBA,"atx",main.getObject(),(int) (150));
 //BA.debugLineNum = 84;BA.debugLine="atx.Run( \"ျမန္မာ ♡ အသည္းပုံ ♡ စာလုံးပုံစံျဖစ္ပါသည္";
mostCurrent._atx._run("ျမန္မာ ♡ အသည္းပုံ ♡ စာလုံးပုံစံျဖစ္ပါသည္။",(anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(mostCurrent._lb.getObject())));
 //BA.debugLineNum = 86;BA.debugLine="b1.Initialize(\"b1\")";
mostCurrent._b1.Initialize(mostCurrent.activityBA,"b1");
 //BA.debugLineNum = 87;BA.debugLine="Dim b1bg As ColorDrawable";
_b1bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 88;BA.debugLine="b1bg.Initialize(Colors.Black,10)";
_b1bg.Initialize(anywheresoftware.b4a.keywords.Common.Colors.Black,(int) (10));
 //BA.debugLineNum = 89;BA.debugLine="b1.Text = \"Install\"";
mostCurrent._b1.setText((Object)("Install"));
 //BA.debugLineNum = 90;BA.debugLine="b1.Background = b1bg";
mostCurrent._b1.setBackground((android.graphics.drawable.Drawable)(_b1bg.getObject()));
 //BA.debugLineNum = 91;BA.debugLine="Activity.AddView(b1,20%x,(lb.Top+lb.Height)+1%y,60";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._b1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),(int) ((mostCurrent._lb.getTop()+mostCurrent._lb.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 93;BA.debugLine="b2.Initialize(\"b2\")";
mostCurrent._b2.Initialize(mostCurrent.activityBA,"b2");
 //BA.debugLineNum = 94;BA.debugLine="b2.Background = b1bg";
mostCurrent._b2.setBackground((android.graphics.drawable.Drawable)(_b1bg.getObject()));
 //BA.debugLineNum = 95;BA.debugLine="b2.Text = \"Change Font\"";
mostCurrent._b2.setText((Object)("Change Font"));
 //BA.debugLineNum = 96;BA.debugLine="Activity.AddView(b2,20%x,(b1.Top+b1.Height)+2%y,60";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._b2.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),(int) ((mostCurrent._b1.getTop()+mostCurrent._b1.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 98;BA.debugLine="b3.Initialize(\"b3\")";
mostCurrent._b3.Initialize(mostCurrent.activityBA,"b3");
 //BA.debugLineNum = 99;BA.debugLine="b3.Text = \"About App\"";
mostCurrent._b3.setText((Object)("About App"));
 //BA.debugLineNum = 100;BA.debugLine="b3.Background = b1bg";
mostCurrent._b3.setBackground((android.graphics.drawable.Drawable)(_b1bg.getObject()));
 //BA.debugLineNum = 101;BA.debugLine="Activity.AddView(b3,20%x,(b2.Top+b2.Height)+2%y,60";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._b3.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),(int) ((mostCurrent._b2.getTop()+mostCurrent._b2.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 103;BA.debugLine="lfoot.Initialize(\"ft\")";
mostCurrent._lfoot.Initialize(mostCurrent.activityBA,"ft");
 //BA.debugLineNum = 104;BA.debugLine="lfoot.Text = \"Developed By Khun Htetz Naing\"";
mostCurrent._lfoot.setText((Object)("Developed By Khun Htetz Naing"));
 //BA.debugLineNum = 105;BA.debugLine="lfoot.TextColor = Colors.Magenta";
mostCurrent._lfoot.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Magenta);
 //BA.debugLineNum = 106;BA.debugLine="lfoot.Gravity = Gravity.CENTER";
mostCurrent._lfoot.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 107;BA.debugLine="Activity.AddView(lfoot,0%x,95%y,100%x,5%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lfoot.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (95),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (5),mostCurrent.activityBA));
 //BA.debugLineNum = 108;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 239;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 241;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 208;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 209;BA.debugLine="If File.Exists(File.DirRootExternal,\"Heart.apk\") =";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"Heart.apk")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 210;BA.debugLine="File.Delete(File.DirRootExternal,\"Heart.apk\")";
anywheresoftware.b4a.keywords.Common.File.Delete(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"Heart.apk");
 };
 //BA.debugLineNum = 212;BA.debugLine="End Sub";
return "";
}
public static String  _ad1_tick() throws Exception{
 //BA.debugLineNum = 214;BA.debugLine="Sub ad1_Tick";
 //BA.debugLineNum = 215;BA.debugLine="If ph.SdkVersion > 19 Then";
if (mostCurrent._ph.getSdkVersion()>19) { 
 //BA.debugLineNum = 216;BA.debugLine="If Interstitial.Status = Interstitial.Status_AdRe";
if (mostCurrent._interstitial.Status==mostCurrent._interstitial.Status_AdReadyToShow) { 
 //BA.debugLineNum = 217;BA.debugLine="Interstitial.Show";
mostCurrent._interstitial.Show(mostCurrent.activityBA);
 };
 //BA.debugLineNum = 220;BA.debugLine="If Interstitial.Status = Interstitial.Status_Dism";
if (mostCurrent._interstitial.Status==mostCurrent._interstitial.Status_Dismissed) { 
 //BA.debugLineNum = 221;BA.debugLine="Interstitial.LoadAd";
mostCurrent._interstitial.LoadAd(mostCurrent.activityBA);
 };
 };
 //BA.debugLineNum = 224;BA.debugLine="ad1.Enabled = False";
_ad1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 225;BA.debugLine="End Sub";
return "";
}
public static String  _ad2_tick() throws Exception{
 //BA.debugLineNum = 227;BA.debugLine="Sub ad2_Tick";
 //BA.debugLineNum = 228;BA.debugLine="If ph.SdkVersion > 19 Then";
if (mostCurrent._ph.getSdkVersion()>19) { 
 //BA.debugLineNum = 229;BA.debugLine="If Interstitial.Status = Interstitial.Status_AdRe";
if (mostCurrent._interstitial.Status==mostCurrent._interstitial.Status_AdReadyToShow) { 
 //BA.debugLineNum = 230;BA.debugLine="Interstitial.Show";
mostCurrent._interstitial.Show(mostCurrent.activityBA);
 };
 //BA.debugLineNum = 233;BA.debugLine="If Interstitial.Status = Interstitial.Status_Dism";
if (mostCurrent._interstitial.Status==mostCurrent._interstitial.Status_Dismissed) { 
 //BA.debugLineNum = 234;BA.debugLine="Interstitial.LoadAd";
mostCurrent._interstitial.LoadAd(mostCurrent.activityBA);
 };
 };
 //BA.debugLineNum = 237;BA.debugLine="End Sub";
return "";
}
public static String  _b1_click() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _i = null;
 //BA.debugLineNum = 179;BA.debugLine="Sub b1_Click";
 //BA.debugLineNum = 180;BA.debugLine="If ph.SdkVersion > 19 Then";
if (mostCurrent._ph.getSdkVersion()>19) { 
 //BA.debugLineNum = 181;BA.debugLine="ad1.Enabled = True";
_ad1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 183;BA.debugLine="Msgbox(\"1. Install Font App\" & CRLF & \"2. After Cl";
anywheresoftware.b4a.keywords.Common.Msgbox("1. Install Font App"+anywheresoftware.b4a.keywords.Common.CRLF+"2. After Click Change Font and Choose your Font!","Attention!",mostCurrent.activityBA);
 //BA.debugLineNum = 184;BA.debugLine="File.Copy(File.DirAssets,\"Heart.apk\",File.DirRoot";
anywheresoftware.b4a.keywords.Common.File.Copy(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Heart.apk",anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"Heart.apk");
 //BA.debugLineNum = 185;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 186;BA.debugLine="i.Initialize(i.ACTION_VIEW,\"file:///\"&File.Dir";
_i.Initialize(_i.ACTION_VIEW,"file:///"+anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/Heart.apk");
 //BA.debugLineNum = 187;BA.debugLine="i.SetType(\"application/vnd.android.package-archiv";
_i.SetType("application/vnd.android.package-archive");
 //BA.debugLineNum = 188;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 //BA.debugLineNum = 189;BA.debugLine="End Sub";
return "";
}
public static String  _b2_click() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _abc = null;
 //BA.debugLineNum = 191;BA.debugLine="Sub b2_Click";
 //BA.debugLineNum = 192;BA.debugLine="If ph.SdkVersion > 19 Then";
if (mostCurrent._ph.getSdkVersion()>19) { 
 //BA.debugLineNum = 193;BA.debugLine="ad1.Enabled = True";
_ad1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 195;BA.debugLine="ToastMessageShow(\"1. Click Font Style Or Font >";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("1. Click Font Style Or Font > "+anywheresoftware.b4a.keywords.Common.CRLF+"2. Choose Myanmar Heart(iFont)",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 196;BA.debugLine="Dim abc As Intent";
_abc = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 197;BA.debugLine="abc.Initialize(\"android.settings.DISPLAY_SETTINGS";
_abc.Initialize("android.settings.DISPLAY_SETTINGS","");
 //BA.debugLineNum = 198;BA.debugLine="StartActivity(abc)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_abc.getObject()));
 //BA.debugLineNum = 199;BA.debugLine="End Sub";
return "";
}
public static String  _b3_click() throws Exception{
 //BA.debugLineNum = 201;BA.debugLine="Sub b3_Click";
 //BA.debugLineNum = 202;BA.debugLine="If ph.SdkVersion > 19 Then";
if (mostCurrent._ph.getSdkVersion()>19) { 
 //BA.debugLineNum = 203;BA.debugLine="ad1.Enabled = True";
_ad1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 205;BA.debugLine="Msgbox(\"App Name : Myanmar Heart Font\" & CRLF & \"";
anywheresoftware.b4a.keywords.Common.Msgbox("App Name : Myanmar Heart Font"+anywheresoftware.b4a.keywords.Common.CRLF+"Version : 1.0"+anywheresoftware.b4a.keywords.Common.CRLF+"Dev  By : Khun Htetz Naing"+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Special Thank to Font Dev!","About App",mostCurrent.activityBA);
 //BA.debugLineNum = 206;BA.debugLine="End Sub";
return "";
}
public static String  _ft_click() throws Exception{
anywheresoftware.b4a.phone.Phone.PhoneIntents _aa = null;
 //BA.debugLineNum = 110;BA.debugLine="Sub ft_Click";
 //BA.debugLineNum = 111;BA.debugLine="Dim aa As PhoneIntents";
_aa = new anywheresoftware.b4a.phone.Phone.PhoneIntents();
 //BA.debugLineNum = 112;BA.debugLine="StartActivity(aa.OpenBrowser(\"https://www.faceboo";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_aa.OpenBrowser("https://www.facebook.com/Khun.Htetz.Naing")));
 //BA.debugLineNum = 113;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 21;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 22;BA.debugLine="Dim imv,imv1,imv2,imv3 As ImageView";
mostCurrent._imv = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._imv1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._imv2 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._imv3 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Dim iv1,iv2,iv3,iv4 As BitmapDrawable";
mostCurrent._iv1 = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._iv2 = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._iv3 = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._iv4 = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 24;BA.debugLine="Dim ani As ICOSSlideAnimation";
mostCurrent._ani = new giuseppe.salvi.icos.library.ICOSSlideAnimation();
 //BA.debugLineNum = 25;BA.debugLine="Dim ph As Phone";
mostCurrent._ph = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 26;BA.debugLine="Dim atx As AnimateText";
mostCurrent._atx = new com.htoophyoe.anitext.animatetext();
 //BA.debugLineNum = 27;BA.debugLine="Dim b1,b2,b3 As Button";
mostCurrent._b1 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._b2 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._b3 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Dim mm As Typeface : mm = mm.LoadFromAssets(\"Myanm";
mostCurrent._mm = new anywheresoftware.b4a.keywords.constants.TypefaceWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Dim mm As Typeface : mm = mm.LoadFromAssets(\"Myanm";
mostCurrent._mm.setObject((android.graphics.Typeface)(mostCurrent._mm.LoadFromAssets("Myanmar Heart.ttf")));
 //BA.debugLineNum = 29;BA.debugLine="Dim lb,lfoot As Label";
mostCurrent._lb = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lfoot = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Dim Banner As AdView";
mostCurrent._banner = new anywheresoftware.b4a.admobwrapper.AdViewWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Dim Interstitial As mwAdmobInterstitial";
mostCurrent._interstitial = new mobi.mindware.admob.interstitial.AdmobInterstitialsAds();
 //BA.debugLineNum = 32;BA.debugLine="End Sub";
return "";
}
public static String  _imv_click() throws Exception{
anywheresoftware.b4a.phone.Phone.PhoneIntents _i = null;
 //BA.debugLineNum = 147;BA.debugLine="Sub imv_Click";
 //BA.debugLineNum = 148;BA.debugLine="If ph.SdkVersion > 19 Then";
if (mostCurrent._ph.getSdkVersion()>19) { 
 //BA.debugLineNum = 149;BA.debugLine="ad1.Enabled = True";
_ad1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 151;BA.debugLine="Dim i As PhoneIntents";
_i = new anywheresoftware.b4a.phone.Phone.PhoneIntents();
 //BA.debugLineNum = 152;BA.debugLine="StartActivity(i.OpenBrowser(\"https://play.google.";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.OpenBrowser("https://play.google.com/store/apps/details?id=com.htetznaing.cocmmtool")));
 //BA.debugLineNum = 153;BA.debugLine="End Sub";
return "";
}
public static String  _imv1_click() throws Exception{
anywheresoftware.b4a.phone.Phone.PhoneIntents _i = null;
 //BA.debugLineNum = 155;BA.debugLine="Sub imv1_Click";
 //BA.debugLineNum = 156;BA.debugLine="If ph.SdkVersion > 19 Then";
if (mostCurrent._ph.getSdkVersion()>19) { 
 //BA.debugLineNum = 157;BA.debugLine="ad1.Enabled = True";
_ad1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 159;BA.debugLine="Dim i As PhoneIntents";
_i = new anywheresoftware.b4a.phone.Phone.PhoneIntents();
 //BA.debugLineNum = 160;BA.debugLine="StartActivity(i.OpenBrowser(\"https://play.google.";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.OpenBrowser("https://play.google.com/store/apps/details?id=com.htetznaing.mmallsimregistration")));
 //BA.debugLineNum = 161;BA.debugLine="End Sub";
return "";
}
public static String  _imv2_click() throws Exception{
anywheresoftware.b4a.phone.Phone.PhoneIntents _i = null;
 //BA.debugLineNum = 163;BA.debugLine="Sub imv2_Click";
 //BA.debugLineNum = 164;BA.debugLine="If ph.SdkVersion > 19 Then";
if (mostCurrent._ph.getSdkVersion()>19) { 
 //BA.debugLineNum = 165;BA.debugLine="ad1.Enabled = True";
_ad1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 167;BA.debugLine="Dim i As PhoneIntents";
_i = new anywheresoftware.b4a.phone.Phone.PhoneIntents();
 //BA.debugLineNum = 168;BA.debugLine="StartActivity(i.OpenBrowser(\"https://play.google.";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.OpenBrowser("https://play.google.com/store/apps/details?id=com.htetznaing.smsunlocker")));
 //BA.debugLineNum = 169;BA.debugLine="End Sub";
return "";
}
public static String  _imv3_click() throws Exception{
anywheresoftware.b4a.phone.Phone.PhoneIntents _i = null;
 //BA.debugLineNum = 171;BA.debugLine="Sub imv3_Click";
 //BA.debugLineNum = 172;BA.debugLine="If ph.SdkVersion > 19 Then";
if (mostCurrent._ph.getSdkVersion()>19) { 
 //BA.debugLineNum = 173;BA.debugLine="ad1.Enabled = True";
_ad1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 175;BA.debugLine="Dim i As PhoneIntents";
_i = new anywheresoftware.b4a.phone.Phone.PhoneIntents();
 //BA.debugLineNum = 176;BA.debugLine="StartActivity(i.OpenBrowser(\"https://play.google.";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.OpenBrowser("https://play.google.com/store/apps/details?id=com.htetznaing.giftappmaker")));
 //BA.debugLineNum = 177;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
starter._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 16;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 17;BA.debugLine="Dim t, t1,t2,t3 As Timer";
_t = new anywheresoftware.b4a.objects.Timer();
_t1 = new anywheresoftware.b4a.objects.Timer();
_t2 = new anywheresoftware.b4a.objects.Timer();
_t3 = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 18;BA.debugLine="Dim ad1,ad2 As Timer";
_ad1 = new anywheresoftware.b4a.objects.Timer();
_ad2 = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 19;BA.debugLine="End Sub";
return "";
}
public static String  _t_tick() throws Exception{
 //BA.debugLineNum = 115;BA.debugLine="Sub t_Tick";
 //BA.debugLineNum = 116;BA.debugLine="imv.RemoveView";
mostCurrent._imv.RemoveView();
 //BA.debugLineNum = 117;BA.debugLine="Activity.AddView(imv,0%x,0%x,100%x,170dip)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._imv.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (170)));
 //BA.debugLineNum = 118;BA.debugLine="ani.StartAnim(imv)";
mostCurrent._ani.StartAnim((android.view.View)(mostCurrent._imv.getObject()));
 //BA.debugLineNum = 119;BA.debugLine="t.Enabled = False";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 120;BA.debugLine="t1.Enabled = True";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 121;BA.debugLine="End Sub";
return "";
}
public static String  _t1_tick() throws Exception{
 //BA.debugLineNum = 123;BA.debugLine="Sub t1_Tick";
 //BA.debugLineNum = 124;BA.debugLine="imv1.RemoveView";
mostCurrent._imv1.RemoveView();
 //BA.debugLineNum = 125;BA.debugLine="Activity.AddView(imv1,0%x,0%y,100%x,170dip)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._imv1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (170)));
 //BA.debugLineNum = 126;BA.debugLine="ani.StartAnim(imv1)";
mostCurrent._ani.StartAnim((android.view.View)(mostCurrent._imv1.getObject()));
 //BA.debugLineNum = 127;BA.debugLine="t1.Enabled = False";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 128;BA.debugLine="t2.Enabled = True";
_t2.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 129;BA.debugLine="End Sub";
return "";
}
public static String  _t2_tick() throws Exception{
 //BA.debugLineNum = 131;BA.debugLine="Sub t2_Tick";
 //BA.debugLineNum = 132;BA.debugLine="imv2.RemoveView";
mostCurrent._imv2.RemoveView();
 //BA.debugLineNum = 133;BA.debugLine="Activity.AddView(imv2,0%x,0%y,100%x,170dip)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._imv2.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (170)));
 //BA.debugLineNum = 134;BA.debugLine="ani.StartAnim(imv2)";
mostCurrent._ani.StartAnim((android.view.View)(mostCurrent._imv2.getObject()));
 //BA.debugLineNum = 135;BA.debugLine="t2.Enabled = False";
_t2.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 136;BA.debugLine="t3.Enabled = True";
_t3.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 137;BA.debugLine="End Sub";
return "";
}
public static String  _t3_tick() throws Exception{
 //BA.debugLineNum = 139;BA.debugLine="Sub t3_Tick";
 //BA.debugLineNum = 140;BA.debugLine="imv3.RemoveView";
mostCurrent._imv3.RemoveView();
 //BA.debugLineNum = 141;BA.debugLine="Activity.AddView(imv3,0%x,0%y,100%x,170dip)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._imv3.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (170)));
 //BA.debugLineNum = 142;BA.debugLine="ani.StartAnim(imv3)";
mostCurrent._ani.StartAnim((android.view.View)(mostCurrent._imv3.getObject()));
 //BA.debugLineNum = 143;BA.debugLine="t3.Enabled = False";
_t3.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 144;BA.debugLine="t.Enabled = True";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 145;BA.debugLine="End Sub";
return "";
}
}
