//
//  ViewController.m
//  EngineDemoiOS
//
//  Created by Hongjie Fu on 2021/1/22.
//

#import "GameViewController.h"

#import <XESceneKit/XESceneKit.h>

@interface GameViewController () <XEGameViewDelegate>

@property (nonatomic, strong) XEGameView *gameView;
@property (nonatomic, strong) XSKEngine  *engine;

@end

@implementation GameViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    //创建GameView
    _gameView = [[XEGameView alloc] initWithFrame:self.view.bounds];
    [self.view addSubview:_gameView];
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
    NSString *path = [[[NSBundle mainBundle] bundlePath] stringByAppendingString:@"/GameRes"];
    [engine addLibraryPath:path];
    //为游戏Lua脚本注册原生bridge功能（非必需）
    [engine.scriptBridge regist:self forHandler:@"GameHandler"];
    
    //执行游戏启动脚本
    [engine execteGameScriptFile:@"app"];
}

- (void)viewWillDisappear:(BOOL)animated {
    //移除所有注册的bridge
    [_engine.scriptBridge unregistAll];
    //关闭游戏
    [_gameView stop];
}

//同步Bridge方法
XE_BBRIDGE_METHOD(onGameOver) {
    dispatch_async(dispatch_get_main_queue(), ^{
        UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"提示" message:message preferredStyle:UIAlertControllerStyleAlert];
        [alert addAction:[UIAlertAction actionWithTitle:@"确定" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            [self dismissViewControllerAnimated:YES completion:nil];
        }]];
        [self presentViewController:alert animated:true completion:nil];
    });
    return nil;
}

//异步Bridge方法
XE_BBRIDGE_ASYNC_METHOD(func2) {
    callback(@"OC异步回调");
}

@end
