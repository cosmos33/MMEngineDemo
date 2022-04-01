//
//  RocketGameViewController.m
//  EngineDemoiOS
//
//  Created by HongjieFu on 2022/3/30.
//

#import "RocketGameViewController.h"
#import <XESceneKit/XESceneKit.h>

#define GAME_HANDLER_NAME @"LiveGameHandler"

@interface RocketGameViewController () <XEGameViewDelegate>

//渲染视图，引擎提供
@property (nonatomic, strong)   XEGameView  *gameView;
//火箭配置，DEMO中为空表示火箭编辑模式
@property (nonatomic, copy)     NSString    *rocketConfig;

@end

@implementation RocketGameViewController

- (void)viewDidLoad {
    self.view.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"background"]];
    [super viewDidLoad];
    [self startGame];
}

/**
 * 开始游戏
 */
- (void)startGame {
    CGSize rootViewSize = self.view.bounds.size;
    
    
    //火箭的配置界面，这个界面一般都是用半屏的
    //所以我这里简单设置了一个topMargin
    //当然这里可以根据业务需要自行处理，我只是举例说明如何设置半屏显示
    CGFloat margin = 0;
    if (!_rocketConfig) {
        margin = 300;
    }
    
    _gameView = [[XEGameView alloc] initWithFrame:CGRectMake(0, margin, rootViewSize.width, rootViewSize.height - margin)];
    [self.view addSubview:_gameView];
    _gameView.preferredFramesPerSecond = 30;
    _gameView.renderScale = 1.f;
    _gameView.backgroundColor = [UIColor clearColor];
    _gameView.delegate = self;
    [_gameView start];
}
/**
 * 结束游戏
 */
- (void)stopGame {
    [_gameView stop];
    [_gameView removeFromSuperview];
    _gameView = nil;
}

/**
 * [XEGameView start]的回调，引擎启动成功
 * @param engine 引擎实例对象
 */
- (void)onStart:(id<IXEngine>)engine {
    
    //添加火箭素材目录，可以是任意绝对路径，只有素材在这个路径下找到即可
    NSString *resPath = [[[NSBundle mainBundle] bundlePath] stringByAppendingString:@"/RocketDIY"];
    [engine addLibraryPath:resPath];
    
    //添加头像目录，可以是任意绝对路径，只有素材在这个路径下找到即可
    NSString *avatarsPath = [[[NSBundle mainBundle] bundlePath] stringByAppendingString:@"/Avatars"];
    [engine addLibraryPath:avatarsPath];
    
    //添加第二个头像目录，可以是任意绝对路径，只有素材在这个路径下找到即可
    NSString *avatarsPath2 = [[[NSBundle mainBundle] bundlePath] stringByAppendingString:@"/avatars.bundle"];
    [engine addLibraryPath:avatarsPath2];
    
    //注册Bridge对象，
    //这里需要注意的是 所以的bridge的回调都不在主线程，如果需要操作UI需要调度到主线程中。
    [engine.scriptEngine.scriptBridge regist:self forHandler:GAME_HANDLER_NAME];
    
    if (_rocketConfig) {//送礼模式
        [engine.scriptEngine execteGameScriptFile:@"giftApp"];
        [engine.scriptEngine.scriptBridge callLua:GAME_HANDLER_NAME
                                           action:@"gameInfo"
                                          message:_rocketConfig];
    } else {//编辑模式
        [engine.scriptEngine execteGameScriptFile:@"app"];
    }
}
/**
 * [XEGameView start]的回调，引擎启动失败
 * @param reason 失败原因。
 * 失败原因可能为：1.license授权未通过；2.设备不支持Metal；
 */
- (void)onStartFailed:(NSString *)reason {
    dispatch_async(dispatch_get_main_queue(), ^{
        UIAlertController *alert =
        [UIAlertController alertControllerWithTitle:@"引擎启动失败"
                                            message:reason
                                     preferredStyle:UIAlertControllerStyleAlert];
        
        [alert addAction:[UIAlertAction actionWithTitle:@"确定" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            [self dismissViewControllerAnimated:YES completion:nil];
        }]];
        [self presentViewController:alert animated:true completion:nil];
    });
}

/**
* 游戏结束的回调，发射模式回调。（Bridge方法，游戏回调客户端）
* @param args 无需处理
* @return 无需处理，返回空即可
*/
XE_BBRIDGE_METHOD(removeGame) {
    dispatch_async(dispatch_get_main_queue(), ^{
        [self stopGame];
        
        UIAlertController *alert =
        [UIAlertController alertControllerWithTitle:@"提示"
                                            message:@"火箭发射成功！！！"
                                     preferredStyle:UIAlertControllerStyleAlert];
        
        [alert addAction:[UIAlertAction actionWithTitle:@"确定" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            [self dismissViewControllerAnimated:YES completion:nil];
        }]];
        [self presentViewController:alert animated:true completion:nil];
        
    });
    return nil;
}

/**
* 发射火箭（Bridge方法，游戏回调客户端）
*
* @param args 火箭的样式json
* @return
*/
XE_BBRIDGE_METHOD(startRocket) {
    _rocketConfig = message;
    dispatch_async(dispatch_get_main_queue(), ^{
        //结束火箭编辑
        [self stopGame];
        //开启火箭发射
        [self startGame];
    });
    return nil;
}

/**
* 获取自定义的配置（Bridge方法，游戏回调客户端）
* @param args 无需处理
* @return 头像的jsonArray
*/
XE_BBRIDGE_METHOD(getAvatars) {
    NSArray *avatars =@[
        @"1.jpg",//默认头像  传null的话使用素材内部的默认头像
        @"head_own.png",//第一个是自己头像,(相对于addLibraryPath添加的头像路径的相对路径)
        @"head_anchor.png"//第二个是主播头像,(相对于addLibraryPath添加的头像路径的相对路径)
    ];
    //头像所在的目录就是上面设置的那个头像目录，头像文件必须是一个合法的jpg/png图片。
    //头像必须要带正确的扩展名。
    
    NSData *data = [NSJSONSerialization dataWithJSONObject:avatars
                                                   options:kNilOptions
                                                     error:nil];
    NSString *ret = [[NSString alloc] initWithData:data
                                          encoding:NSUTF8StringEncoding];
    NSLog(@"获取头像:%@", ret);
    return ret;

}

@end
