//
//  AppDelegate.m
//  EngineDemoiOS
//
//  Created by Hongjie Fu on 2021/1/22.
//

#import "AppDelegate.h"

#import <XEngineLua/XEngineLuaModule.h>
#import <XEngineAudio/XEngineAudioModule.h>
#import <XEnginePhysics/XEnginePhysicsModule.h>
#import <XEngineUI/XEngineUIModule.h>
#import <XESceneKit/XESceneKit.h>

@interface AppDelegate ()

@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    
    //启用引擎子模块
    XEngineUseUIModule();
    XEngineUseLuaModule();
    XEngineUseAudioModule();
    XEngineUsePhysicsModule();
    
    //设置引擎的License授权
    [[XEnginePreferences shareInstance] setLicense:@"OjkAHm4HIlGJQc0OL+mtBqDdho7DB8OuLmBwSLaBTncpR3BX3+tjJvX14pWFkfTIty9mIT+bF6O49HQUVYWD6vpWM6rKEqXzCNO9++1YD/hvinWqdoXv41mrLAfU7UgD7W8mXhJgVLdyMwkPOhehZ6kpRIIhzvIeiJdL1j7M00E="];
    
    return YES;
}


#pragma mark - UISceneSession lifecycle


- (UISceneConfiguration *)application:(UIApplication *)application
configurationForConnectingSceneSession:(UISceneSession *)connectingSceneSession
                              options:(UISceneConnectionOptions *)options {
    // Called when a new scene session is being created.
    // Use this method to select a configuration to create the new scene with.
    return [[UISceneConfiguration alloc] initWithName:@"Default Configuration" sessionRole:connectingSceneSession.role];
}


- (void)application:(UIApplication *)application didDiscardSceneSessions:(NSSet<UISceneSession *> *)sceneSessions {
    // Called when the user discards a scene session.
    // If any sessions were discarded while the application was not running, this will be called shortly after application:didFinishLaunchingWithOptions.
    // Use this method to release any resources that were specific to the discarded scenes, as they will not return.
}


@end
