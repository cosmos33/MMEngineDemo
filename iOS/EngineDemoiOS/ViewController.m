//
//  ViewController.m
//  EngineDemoiOS
//
//  Created by Hongjie Fu on 2021/1/22.
//

#import "ViewController.h"
#import "GameViewController.h"
#import "RocketGameViewController.h"

@interface ViewController ()

@property(nonatomic, strong) NSArray *demoData;

@end

@implementation ViewController
    
- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.demoData = @[
        @{@"title": @"定制火箭DEMO", @"subTitle": @"", @"action": ^() {
            RocketGameViewController *vc = [[RocketGameViewController alloc] init];
            vc.modalPresentationStyle = UIModalPresentationFullScreen;
            [self presentViewController:vc animated:YES completion:nil];
        }},
        @{@"title": @"打棒球DEMO", @"subTitle": @"", @"action": ^() {
            GameViewController *vc = [[GameViewController alloc] init];
            vc.modalPresentationStyle = UIModalPresentationFullScreen;
            [self presentViewController:vc animated:YES completion:nil];
        }},
    ];
    
    
}
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
    return @"引擎DEMO";
    
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if (section == 0) {
        return self.demoData.count;
    }
    return 0;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    UITableViewCell *cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:@"cell"];
    
    if (indexPath.section == 0) {
        cell.textLabel.text = self.demoData[indexPath.row][@"title"];
        cell.detailTextLabel.text = self.demoData[indexPath.row][@"subTitle"];
    }
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    if (indexPath.section == 0) {
        void (^action)(void) = self.demoData[indexPath.row][@"action"];
        if (action) {
            action();
        }
    }
    
}

@end
