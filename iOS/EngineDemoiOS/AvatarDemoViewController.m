//
//  AvatarDemoViewController.m
//  EngineDemoiOS
//
//  Created by HongjieFu on 2022/4/13.
//

#import "AvatarDemoViewController.h"
#import <XESceneKit/XESceneKit.h>


@interface AvatarDemoViewController ()<XEGameViewDelegate>

@property (nonatomic, strong) XEGameView *gameView;
@property (nonatomic, strong) XSKEngine  *engine;

@end

@implementation AvatarDemoViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor whiteColor];
    //创建GameView
    _gameView = [[XEGameView alloc] initWithFrame:self.view.bounds];
    [self.view addSubview:_gameView];
    _gameView.backgroundColor = [UIColor clearColor];
    //设置GameView代理
    _gameView.delegate = self;
    //启动GameView
    [_gameView start];
    
}

//GameView启动回调
- (void)onStart:(XSKEngine *)engine {
    //设置引擎日志开关
    [engine setLogEnable:YES];
    //添加引擎资源搜索路径
    NSString *path = [[[NSBundle mainBundle] bundlePath] stringByAppendingString:@"/AvatarDemo"];
    [engine addLibraryPath:path];
    //为游戏Lua脚本注册原生bridge功能（非必需）
    [engine.scriptBridge regist:self forHandler:@"ClientBridge"];
    //执行游戏启动脚本
    [engine execteGameScriptFile:@"app" argument:@""];
}

- (void)onStartFailed:(nonnull NSString *)reason {
    NSLog(@"引擎启动失败%@", reason);
}


- (void)viewWillDisappear:(BOOL)animated {
    //移除所有注册的bridge
    [_engine.scriptBridge unregistAll];
    //关闭游戏
    [_gameView stop];
}

//用户点击退出按钮的回调
XE_BBRIDGE_METHOD(onExitButtonClick) {
    dispatch_async(dispatch_get_main_queue(), ^{
        [self dismissViewControllerAnimated:YES completion:nil];
    });
    return nil;
}

XE_BBRIDGE_METHOD(onSaveButtonClick) {
    NSLog(@"用户点击保存: %@", message);
    return nil;
}


@end
