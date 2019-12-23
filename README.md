# 葫芦娃大作业
## 1.基本操作
      ·空格 重新开始/开始 并记录战斗文本
      ·Q,W,E,A,S,D:改变阵型
      ·L 读取记录文本 
      ·方向键上 加速
      ·方向键下 减速
      .在起始界面/战斗结束/读取战斗结束 时，按下L能选择文本读取，按下空格进入准备阶段，
    准备阶段可以使用QWE调整葫芦娃阵型，使用ASD调整蛇精阵型，再次按下空格开始正常战斗
      ·自动在/rec(/表示jar包所在的根目录)下创建记录文本，若没有rec文件夹则自动创建。
    文本名为record+当前时间戳
      ·加速减速只能在战斗时/读取时使用。若战斗未结束强制取消了窗口，本次战斗记录不会被保存。
## 2.战斗设定
### 2.1 基本战斗
      ·一方全部战死时另一方获胜。可能出现两方同时死亡的平局现象(DRAW)
      ·每一轮，所有活着的生物随机在10*10地图中移动，移动方向为上下左右，移动完后各生物根据攻击范围，寻找目标进行作战。
      ·若攻击范围为x，则可以打到以自身为中心，以2x+1为边长的正方形的所有敌人。
      ·作战时，比较两方攻击力后取随机量，攻击力高的单位获胜概率高。
      ·在50轮会随机刷新4*4毒圈，毒圈外生物全部死亡，毒圈内生物进行最后决战。
      ·若缩圈导致所有生物死亡，上一轮中残留生物多的一方获胜。两方人数一样则平局
      ·运行时，每一轮在战场右边信息框中都会打印出即时战场信息与即时战场警告
      ·击杀会有明显特效，分为近战击杀，四娃火焰击杀，五娃冰冻击杀，蛇精毒雾击杀四种特效。
      ·死亡的生物会在原地留下透明虚影
### 2.2 角色设定
      ·大娃(FirstHlw):攻击力12 攻击范围1
      特性:两回合移动一次;有勇无谋，被围攻时攻击会下降
      ·二娃(SecondHlw):攻击力8 范围1
      特性:千里眼顺风耳，能帮助四娃，五娃扩大攻击范围
      ·三娃(ThirdHlw):攻击力10 范围1
      特性:力大无穷，攻击力相比其他葫芦娃更高
      ·四娃(FourthHlw):攻击力1 范围3
      特性:吐火远程攻击
      ·五娃(FifthHlw):攻击力1 范围3
      特性:冰冻远程攻击
      ·六娃(SixthHlw):攻击力7 范围1
      特性:来无影去无踪，每一轮移动两次
      ·七娃(SeventhHlw):攻击力6 范围1
      特性:最小的葫芦娃，攻击力较低
      ·爷爷(Grandpa):攻击力2 范围1
      特性:几乎无攻击，极度脆弱
      ·蟾蜍(Frog):攻击力5 范围1
      特性:无
      ·蝎子(Scor):攻击力14 范围1
      特性:首领，攻击较高
      ·蛇精(Snake):攻击力5 范围2
      特性:首领 远程毒雾攻击
### 2.3 角色羁绊联系
      ·二娃死亡时，四娃五娃攻击范围降低。二娃是千里眼顺风耳
      ·爷爷死亡时，所有葫芦娃攻击力下降
      ·所有葫芦娃死亡时，爷爷会愤怒，攻击力大幅度提升
      ·蛇精/蝎子精任意一个首领死亡，另一个首领攻击力提高，但所有蟾蜍小怪攻击力降低
## 3.类的设计与系统内部实现
### 3.1 总体类设计
![class](https://github.com/shi1ro/finalproject/blob/master/readmepic/classpic.png)
    
       ·未标出测试类
       ·多使用聚集，生物中使用抽象类继承，阵型中使用接口继承
       ·整体是面向最底层的抽象Creature，然后一层一层向上封装。Creature和BattleMap构成底层，然后是Control整体控制，
     最后图形界面App补上GUI，最后根据要求将main放在Main类中
       ·底层的Creature会被葫芦娃，爷爷，蛇精，蝎子，蟾蜍类来填充。Creature内部的主要线程run函数使用的fight等函数都是
     虚函数，只要上层填充，或者重写一些方法，就能实现不同生物进行不同的移动战斗方式，却使用同一套代码run，极大减少
     了代码复用，做到依赖抽象的依赖倒置
       ·上层Control将多个Creature用ArrayList组合成整体进行整体化操作，为更上层图形界面类提供一些整体化控制所有生物的
     方法。ArrayList<Creature>中只能放入Creature的subclass,由于Creature是抽象类，在Control中会new FirstHlw,
     new Grandpa等，然后放入ArrayList。
       ·同时，上层Control中包含了G1LineUp，G2LineUp两个接口的实例化对象， 表示服务于葫芦娃(group1)的阵型和服务于
     蛇精(group2)的阵型，这两个接口都继承于LineUp这一个阵型最底层接口，接口函数为lineup,lineup函数接受一系列生物
     (ArrayList<Creautre>)，然后来把这些Creature moveTo到相应的地方。G1,G2两个接口分别实现了三个阵型，在准备开始时用
     QWE,ASD调用。
       ·上层Control还应用了工厂模式，包含了一个生物工厂，通过生物工厂的get方法来获得Creature实例化对象。Creature中有许多的
     公用锁，在分别初始化时要反复填写这些相同引用地锁，费时费力。使用生物工厂，先用这些锁初始化生物工厂的锁变量，然后只要给
     生物工厂一个生物编号(如1表示爷爷，2表示大娃，……)和一个初始位置点，就能帮助生物做好全套初始化工作。
       ·最后的App类中使用Control中提供的各种整体性方法，并将其与图形化界面相结合，实现最后的程序逻辑
       ·Main中new一个App，调用App中初始化函数即可
       ·具体类实现见下
### 3.2 Creature类
#### 3.2.1 Creature内部变量
      ·每一个Creature表示一个生物
      ·所有生物随机每个creature都拥有一张共同的地图map,可以通过地图来寻找地方目标，确定下一步移动方向等。
      ·每个creature还含有一个ArrayList<Creature>,用来存取和自己有关的其他生物。在自己死亡时能通过这个ArrayList影响到
    其他生物,达到羁绊联系的效果。
      ·同时，每个creature还有其他一些共用参数，包括攻击范围，攻击力，是否生存，是否移动，是否战斗，位置等参数
#### 3.2.2 Creature的内部重要函数
      ·重要函数大多是虚函数，即依赖接口/抽象，通过继承，来实现不同生物的不同移动模式，不同战斗模式
---
      ·1.run。每个creature都是一个线程，彼此之间要互斥完成移动和战斗。
    所有的生物共享三个锁:移动锁(movelock)，战斗锁(fightlock)，生命锁(lifelock)。这些锁对象中都含有一个int值。
    在运行时，主要通过上层某控制线程与下层所有creature线程共同完成战斗流程。
    大致流程为:
``` java
    while(true)
    {
        synchronized(lifelock)//生命块
        {
          while(...)
             lifelock.wait();
        }
        if(interrupted)//终止判定1
             braek;
        synchronized(movelock)//移动(互斥)块
        {
          while(...)
             movelock.wait();
          //生物进行移动
        }
        if(interrupted)//终止判定2
             braek;
        if(movestate != 0)
        {
          synchronized(fightlock)//战斗(互斥)块
          {
            while(...)
               fightlock.wait();
            //生物进行战斗
          }
        }
    }
```
      ·循环开始
---
      ·先进入生命块sync(lifelock)判定自身lifestate，若自己死亡，则wait在生命锁上，该线程(生物)不参与之后的行动
---
      ·进入移动块sync(movelock)。即进入移动模块。该模块中，movelock值表示本轮所有生物应该互斥移动的次数，自身movestate
    变量表示自己是否已经移动过了。若movelock值为0，或自身的movestate为false，wait在movelock上，不进行任何移动，等待控制
    线程设置movelock后唤醒自己。
      ·控制线程中，先调用每一个creature的ariseMove函数，唤醒所有生物，设置他们的movestate。在creature的ariseMove中，
    若该生物仍存活，则movestate被设为1，表示该生物能移动一次，并返回1，返回值表示该生物本轮想进行的移动次数。若该生物死
    亡直接返回0。
      ·然而存在一些特殊情况:
      ·大娃两轮移动一次，大娃重载的ariseMove中，一轮movestate为1，返回1，一轮movestate为-1，返回0，表示该轮不移动。
      ·同时，六娃每轮动两次，重载函数中movestate设为2并返回2。
      ·控制线程调用所有的生物的ariseMove，收集所有返回值相加，得到本轮应该进行的移动次数，将该次数赋值给movelock，然后
    唤醒所有wait在movelock上的生物。同时，控制线程也进入wait，等待movelock为0，即直到所有生物移动完成。
      ·此时又回到各个creature的run，movestate被置为非0，movelock也非0，各个creature从movelock中醒来，由于仍在movelock
    同步块中，此时各个creature开始互斥移动，移动完后，movestate-1,movelock-1,当movelock减为0时，表示这是最后一个移动的
    creature,该线程负责唤醒控制线程。之后退出互斥块。
      ·再回头看大娃，六娃的特例。
      ·在大娃的停滞回合，movestate为-1而不是0，能让大娃从while wait中离开，进入后面的战斗互斥块。所以在移动互斥块内部
    中有一段:if movelock==-1时，不移动直接退出移动互斥块。该段代码即针对这个特例。
      ·而对于六娃，如果movestate非0，就不能进入下方战斗互斥块，六娃在第一次移动后movestate为1，会跳过下方战斗互斥块，
    回到循环开头。只移动六娃不可能死亡，他将跳过生命块，重新进入移动块执行一次移动，从而完成移动两次的要求
---
      ·退出movelock互斥块后，进入fightlock互斥块。
      ·由于没有生物一轮多次攻击，或者多轮一次攻击，战斗部分相对没移动部分复杂。
      ·生物进入战斗互斥块后，与move类似，若fightlock为0，或自身fightstate为0，wait在fightlock上。等待控制线程设置
    fightlock。
      ·控制线程中，遍历所有生物，若该生物仍存活，调用其arisefight唤醒战斗，即将他们的fightlock设为1，将存活的生物
    数量相加，得到fightlock值，然后fightlock.notifyAll。生物被唤醒后，执行虚函数fight进行战斗，之后fightstate--,
    fightlock--，保证自身在不被唤醒时不会进入下一次战斗
      ·最后退出战斗互斥块，重新回到循环开头
---
---
      ·2.虚函数fight每个生物战斗方式不同，需要继承者实现。大体上是根据地图寻找在攻击范围的敌人，然后取随机值
    判定胜负。攻击越高，胜率越高。负的一方调用其die函数。
---
      ·3.die 让该生物体死亡(lifestate置0)，并通过一个arraylist<Creature>，获得关联生物的引用，调用他们的effect
    产生一些影响。如每个葫芦娃的arraylist中都装着爷爷，葫芦娃死亡时，调用爷爷effect，影响爷爷的战斗属性
---
      ·4.虚函数effect 表示其他生物死亡时对该生物造成的影响，比如所有葫芦娃死去时，爷爷攻击力大幅度上升，就是通过
    爷爷类中实现的effect完成的
---
      ·5.moveTo,通过使用每个生物都共用的地图，在地图上探索上下左右四个方向是否可以通行。若是边界，或该区域有人，则表示
    该方向被堵。四方向均被堵则该生物本轮不行动。在剩下的可以移动的方向中，随机选择一个方向移动。
---
      ·6.getRecord,getWarning:Creature需要朝上层调用者发送一些信息，比如这一轮该生物移动到了哪，这一轮该生物和谁进行
    了战斗,战斗结果如何，等等。Creature中有String变量record和warning，每进行一些内部行为时，就在这些String后append新信息
    并提供get函数给上层使用，get函数return String,并把String置空，准备下一轮。普通的移动战斗放入record中，一些特殊信息，
    如死亡的二娃会减少四娃五娃的攻击范围，会放入warning中，在最上层图形界面中会用红色字体标出。
    
### 3.3 BattleMap类
      ·维护一个10*10数组，保持Creature中保存的位置变量(x,y)与他们在数组地图中的位置的一致。当某生物死亡时，在Creature中
    (x,y)不变，但在数组地图中直接移除。地图只用一个特定char标号标记某生物。如O表示空地,A表示大娃，B表示二娃，F表示蟾蜍，
    由于蟾蜍有多个还需要一个int变量区分不同蟾蜍。所以BattleMap主要维护char[][],int[][]两个数组。同时，由于会有缩圈情况
    地图边界也有变量xmin,xmax,ymin,ymax进行控制
      ·为Creature提供一些帮助支持，如根据生物位置即提供的攻击范围来帮助生物定位攻击范围的敌人(findEnemy方法),帮助生物
    探索四个方向是否被阻挡(creatureTryMove方法)
      ·实现较为简单粗暴，此不赘述
### 3.4 Control类
      ·负责对所有生物提供整体化控制。其中有BattleMap对象(和所有生物相同的地图)和ArrayList<Creature>对象。初期将所有生物
    塞入ArrayList后，给BattleMap复制一份一样的ArrayList，方便BattleMap实现其提供给Creature的功能
      ·如初始化所有生物，为他们设定起始位置(未实现队形，位置在初始化时静态给出)，为这些生物准备Thread,提供方法终止所有生物
    线程，胜负判定，缩毒圈时的死亡判定，等等。一些set,get的逻辑简单部分略去，此处说明一些其他内容
      ·线程中断:由于Creature的run中使用while(true)语句块，需要上层通知他使其结束。Creature中有一个boolean变量interrupted
    (下简称i),i初始为false。在while(true)中，每一轮fight结束后，生物可能死亡，被lock在lifelick上，未死亡生物则lock在
    movelock上若要强行终止这些线程，控制端将所有生物的i置true,然后进入sync(lifelock)块,将他们全体“复活”
    (全体lifestate置1),然后唤醒他们。Creature的run中，sync(lifelock)块后紧接着i判定，若i为true直接break。然后对卡在
    move块的生物做类似操作，run的move块之后也有i判定致此所有生物跳出while块，结束线程。
      ·缩圈：随机取数后设置地图边界，并将处在圈外的所有生物全部杀死(调用die)，返回一串字符串，含有地图边界信息和因缩圈死亡
    的生物信息。记录因缩圈而死亡的两方人数
      ·判定胜负：遍历所有生物，若有一方lifestate全为0，判另一方胜利。若全体死亡，必然是50轮缩圈导致，进一步检查缩圈死亡人
    数来判断是一方胜利还是平局
### 3.5 App类(上层应用核心类)
#### 3.5.0 整体介绍
      ·最上层图形界面类。控制程序流程。
      ·图形界面使用javafx,main函数launch后传给start一个stage，在舞台上设置scene,group,pane完成需求。
      ·在图形界面中，需要使用的资源有:
      葫芦娃，蛇精，蝎子精，蟾蜍的生物图像
      战斗特效图像
      战场背景图像
      两个文字框打印即时信息
      ·这些资源保存在/src/main/resources中，会被一并打包进jar
#### 3.5.1 主要逻辑
      ·initUI函数初始化所需图形界面组件，主要是在scene下用一个group包含所有图片和文本框来进行整体控制。之后注册键盘事件，再
    之后通过变量state来控制程序，初始state为0
      ·键盘事件中，若在初始state=0时按下空格，state置1，表示进入战斗，此后除了调整运行速度，无法进行任何操作，战斗结束
    后state置0战斗过程中，开启一个新线程进行战斗。若在初始state=0时按下L，调出FileChooser，选择一个文件进行读取，
    state置2，同样，此后 除了调整运行速度，无法进行任何操作。也同样开启一个新线程模拟战斗若直接取消主窗口，state置-1，
    结束所有线程。如果此时正在进行战斗，则判定为异常结束，战斗记录不会被保存。
#### 3.5.2 线程控制
---
    1.javafx线程初探
      ·javafx中，只能在javafx的主线程中，才能对组件进行控制操作，如调整生物头像坐标，让生物头像变灰表示死亡等
      ·在构建初期，我曾尝试在javafx主线程中令生物体移动战斗，然后每一轮结束立刻更新界面，体现出生物体的移动和死亡。但是，主
    线程中会出现先移动战斗了好几轮，才回头刷新一次界面的情况。
      ·例如，要达成的目标是:生物体A从(1,0)-(1,1)-->刷新界面让生物体头像移动至(1,1)，sleep-->生物体移动至(1,2)-->刷新界面让
    生物体移动至(1,2),sleep....。但是，若在javafx主线程直接使用如上串行逻辑，在刷新时，生物体直接跳到(1,2),(1,1)后的sleep
    也不起效果。
      ·不仅一步步串行进行。并且刷新界面时时常跳BUG，显示scene中的group中某个arraylist的index超过bound，推测是因为多次刷新
    交织在一起的非互斥刷新导致的
      ·之后又继续进行尝试，在注册键盘事件的函数中再开辟一个线程，在该线程中尝试计算-刷新-计算-刷新的串行模式，发现可行，不会
    出现一次更新跳跃多次计算的情况。但是仍然会出现group中某arraylist下标超限的迷之BUG。
      ·起初猜测是文本的问题。在exception.printstacktrace中出现了类似"文本框边界计算超限"的提示。文本框直接用Text，设置不了
    边界，每一轮战斗信息可长可短，计算边界出错似乎理所应当，同时在窗口中也会出现文本框不能同步刷新，和上一轮的文本框有时会相互
    重叠导致界面显示近乎乱码崩溃，但是图像移动的刷新又显得很正常。曾一度想过放弃文本框，只显示战斗界面，这样刷新界面时不会出错。
      ·多方搜寻后得知，在非javafx主线程中，要对组件进行操作需要使用Platform.runLater(Thread)。在重构框架，所有刷新界面出都
    使用runLater后，界面能保持串行计算-刷新-计算-刷新流程并且刷新不崩溃。在对图像ImageView使用setCache后，刷新显得更加流畅。
    之后又使用runLater增加了部分攻击特效，也能保持流畅运行。
      ·最终，整体线程规划大致如下：
      ·javafx主线程->键盘空格的战斗线程，键盘L的读取线程->Creature线程，组件更新Platform.runLater线程。
      ·其中，空格战斗线程需要和Creature线程达成同步，为程序核心线程，逻辑如下
---
    2.组件更新线程
      ·部分组件更新使用Lamda表达式，如Platform.runLater(()->updateSPPlace());,直接使用一行代码调用相关函数，更新组件，
    简单方便
      ·展示特效也需要使用组件更新的Platform.runLater,具体用函数effectShow(String str)，接受一个战斗信息函数str,根据str
    的战斗信息，在不同地方展现不同的普通攻击，远程攻击特效。先runLater,将特效图像放到指定地点，显示图像setVisible(true)，
    sleep一定时间后再runLater setVisible(false)，体现一种闪现特效的效果。
---
    3.战斗线程
``` java
    inititilize...
    Thread thread = new Thread() 
    {
      @Override
      public void run() 
      {
          hlwcontrol.threadStart();
          while(true)
          {
            normalBattleRoundBegin();
            if(roundtime == contracttime)
            {
               if(normalBattleContraction() == 0)
                  break;
               continue;
            }
            synchronized(hlwcontrol.movelock)
            {
              normalBattleOneRoundMove();
            }
            normalBattleHandleAfterMove();
            synchronized(hlwcontrol.fightlock)
            {
              normalBattleOneRoundFight();
            }
            if(normalBattleHandleAfterFight() == 0)
              break;

          }
          hlwcontrol.threadInterrupt();
          fileOutClose();
          if(state == -1)
          {
              fo.delete();
          }
          state = 0;
       }
     };
     thread.start();
```
      ·按下空格键后进入该段代码
      ·首先会初始化，初始化内容包括:
      将文本框内容置为“Fight is ready to begin”
      重新申请一个Control对象，命名为hlwcontrol。申请完后，在Control构造函数中会自动初始化所有葫芦娃位置信息，战斗信息。
      申请得到Control中的葫芦娃容器，根据每个葫芦娃位置，将对应头像放在对应处。部分变灰头像复原
      在/rec/下新建一个txt记录战斗。txt名称为record+当前时间戳
      ·然后开启一个内部类线程，直接start该线程，空格监听器函数就结束了
      ·在内部类线程中，先开启所有的Creature线程，然后进入while(true)
      ·开始：normalBattleRoundBegin,增加roundtime,在屏幕上打出round x begin。如果是第一轮，再sleep一会儿，看清初始阵型
      ·if块：若当前是50轮，进入缩圈函数normalBattleContraction。缩圈函数中，调用hlwcontrol的缩圈，在地图中画一个红色矩形
    然后获得生物的死亡信息，更新在屏幕上，然后更新头像，展示特效，进入胜利条件判定，若有一方胜利/平局，直接break跳出while
    结束线程
      ·进入移动同步块，开始一轮移动(normalBattleOneRoundMove函数)内部实现为：调用hlwcontrol的initCreaMoveState，
    唤醒所有生物movestate，让生物开始移动然后wait，等待生物移动完毕
      ·normalBattleHandleAfterMove：移动后更新界面，屏幕打印信息，同时控制台打印一些信息方便DEBUG，最终版中不会在控制台
    打印信息
      ·进入战斗同步块，与移动同步块类似，经过唤醒-等待后，更新界面，屏幕打印信息，显示战斗特效
      ·战斗结束后，由于有生物死亡，需要进行一次胜利条件判定，若能决出胜负就break结束线程
      ·退出while块后，先把所有Creature打断(threadInterrupt)，然后关闭记录文件。如果是被强制退出，在注册过的监听器中，
    state会被置于-1。而state为-1时，当前的记录文件会被删除，保持记录文件完整性
---
    4.读取线程
      ·同样需要新开一个线程，不能在javafx中串行计算-更新
      ·仍然使用和战斗一样的初始函数，申请一个hlowcontrol，但是不使用其中的Creature线程
      ·使用FileChooser，调出文本选择框，默认显示路径是jar所在根目录的/rec/
      ·选择文本，正常读取文本后，会先看开头是否有一串指定字符串identify，如果比较错误，表示这不是我的程序产生的记录，直接
    结束，不读取该文本
      ·identify通过后，开始解析记录。记录文本中，开始有一段
      Place Initial
      xxx (x,y)
      xxx (x,y)
      ...
      Initial End
      ·这一段是按照hlwcontrol中的ArrayList顺序记录所有生物初始位置的。所以，读取时候，也申请一个hlwcontrol，按一个顺序
    进行初始化即可。同时，初始化过程中，使用一个HashMap<String,Integer>,通过生物名字对应上一个Integer的数组下标，建立
    hash索引，方便之后使用
      ·然后根据move,defeat,distant defeat,die of poison等关键词，解析生物行动，指定生物移动，刷新头像，更新屏幕文本，
    展现特效，短暂sleep，直到读取文本结束
      ·读取文本结束，表示已经结束，判定胜负，退出线程即可
---

#### 3.5.3 资源路径
      ·起初，使用vscode编程，在读取图片，或者确定/rec/文件夹时，就简单使用如/pic/的路径，然后将所需图片放入/pic/即可
      ·但是在mvn package最后生成jar包时，会锁定到jar包所在根目录 然后/pic/,图片并没有跟着复制到该目录，然后读取失败
      ·所以改变路径获取方式，先在src/main下创建resources文件夹，在其中放上pic文件夹，mvn package时会自动将这些
    图片打包进jar包
      ·然后使用getClass().getResourceAsStream(url)，可以安全读取jar包中的资源
      ·对于文件存取读取，使用getClass().getProtectionDomain().getCodeSource().getLocation().getPath()，会得到
    /../../../.jar，字符串处理后，将该路径改为/../../../rec/，若该文件夹不存在，创建文件夹。这部分操作在程序开始时就
    执行。
      ·这样，无论jar在什么位置，一是都能读取到jar包内的图片，二是都能在jar所在目录创建rec,不影响之后的saveload。
### 3.6 Test类
      ·由于程序先大体完成，上课才到测试部分，早期测试DEBUG都是通过编译器完成的，同时多处涉及多线程，测试类编写不方便，
    所以只是对少量方法做了少量测试，主要是熟悉如何编写测试类
      ·先使用和主类同样package,能使用其他主类
      ·一是测试生物移动，死亡和地图是否同步。由于地图上有生物信息，生物内部也有一个Point变量，保持两个变量同步是
    很重要的
      ·以下判定都使用assertTrue(condition)
      ·testCreatureMove():先创建一个大娃(FirstHlw),指定初始位置(5,5)，然后判定map地图上[5][5]处是否是大娃，然后调用
    大娃自身moveTo,移动到5,6，再分别判定[5][5]和[5][6]处的个体，然后再在(5,5)处创建一个二娃，看地图中是否都能同步生成
      ·testCreatureDie():创建一个爷爷和大娃，用一个arraylist让大娃关联上爷爷，调用爷爷的die(),首先爷爷内部(x,y)不变，
    因为需要在死亡处生成一个“尸体”，而在map中，爷爷则被移除，否则留着可能会堵着别人移动，或者被人“再杀一次”。同时，die
    还会使关联生物大娃的攻击降低，也要对此进行测试。该函数就是测试程序是否与以上内容匹配
      ·testCreatureMoveBlock():如果一个生物在角落，旁边四个方向要不然是边界，要不然是其他生物，则该生物不移动。该函数中，
    先创建一个在(9,0)的大娃，再在(8,0)(9,1)处都创建生物，把大娃卡在角落，调用地图中creatureTryMove,传入大娃，然后判断大娃
    位置是否变化，若不变则测试通过
      ·以上测试都在类CreatureAndMapSyncTest中
      ·ContractTest()：测试缩圈，先new一个Control，执行一次缩圈，判定圈内生物是否都活着，圈外生物是否都死亡。主要测试地图
    边界是否被设定好，以及生物是否正常死亡
      ·该测试在类ControlTest中
      ·类ResourceTest中尝试对图片资源进行测试，但是由于jar打包后资源路径相对于.class会变化，jar路径也会变化，要定位到指定
    地点较为困难，就放弃了该类测试，源文件代码全部被注释，不再使用
### 3.7 其他核心类
      ·CreatureFactory：生物工厂，整体化地生产不同生物实例，减少重复代码量，内部只有一个构造函数和get函数，
      ·LineUp及其implements：阵型接口，专门负责为葫芦娃排队，G1LineUp的implements实现将葫芦娃排在左边，G2LineUp将蛇精排
    在右边
### 3.8 其他辅助类
      ·Mylock：一开始准备使用Integer，发现使用等号时是传值，导致不同生物wait在不同地方。所以新建了一个class，专门负责锁，
    其中只有一个公开变量int x，方便操作。
      ·Point：其中有int x,int y,重载了toString，返回(x,y)样式字符串，大体上当成一个struct{}使用。
      ·Direction：内部有一个static int方向数组，存了八个方向，一个static int direcnum，表示使用几个方向。程序中使用4个
    方向，direcnum被设为4。类似当做c里的全局变量使用
## 4.程序效果截图
    1.mvn test clean package可以运行
![mvnpackage1](https://github.com/shi1ro/finalproject/blob/master/readmepic/mvn%201.jpg)
![mvnpackage2](https://github.com/shi1ro/finalproject/blob/master/readmepic/mvn2.jpg)
![mvnpackage3](https://github.com/shi1ro/finalproject/blob/master/readmepic/mvn3.jpg)
    
    2.命令行jar 运行
![runjar](https://github.com/shi1ro/finalproject/blob/master/readmepic/jar%E8%BF%90%E8%A1%8C.jpg)
    
    可以看到执行后窗口已经出现，可以开始战斗
    3.战斗界面
![running1](https://github.com/shi1ro/finalproject/blob/master/readmepic/fight1.jpg)
![running2](https://github.com/shi1ro/finalproject/blob/master/readmepic/fight2.jpg)
     
    正常战斗，可以看到战斗特效与右方信息栏
![running3](https://github.com/shi1ro/finalproject/blob/master/readmepic/contract.jpg)
      
    缩圈，全场毒雾特效
![running4](https://github.com/shi1ro/finalproject/blob/master/readmepic/contract1.jpg)
         
    缩圈时战斗信息
![load1](https://github.com/shi1ro/finalproject/blob/master/readmepic/load.jpg)
![load2](https://github.com/shi1ro/finalproject/blob/master/readmepic/load1.jpg)
    
    正常读取时，跳出文本选择框，选择后信息栏会显示相关信息

## 5.其他/感想
      ·不足：
      ·异常处理只在一些文件输入输出中使用，泛型只使用了一个ArrayList<Creature>,抹除到Creature，上层能使用Creature
    的基本函数。注解也没有用到。
      ·类设计较为简单，部分变量的访问控制较为含糊，第一次使用java设计大工程，并初次使用javafx,多有不足，许多东西都难以
    按照设想实现，最后只能向时间妥协，往简单方便处理
      ·感想：
      ·在整个过程经过了许多次重构，由于某次改变了maven根目录，环境变量忘了改，又导致各种雪崩式BUG，vscode使用也不够熟练
    一些错误让人摸不着头脑。只能沉住气，不心急，相信自己能行，将BUG从大化小，从小化了，尽量从根源处解决BUG，先了解一些关于
    javafx或其他使用类的基础实现，才能更好理解示例代码
      ·许多地方写得过于static,不大dynamic,同时写好的底层框架基本把自己关住了，一些后期想到的功能难以添加，又没有时间重构，
    只能作罢，感觉可更新上限也很低，过于“一次性”。让我更加感受到了底层构建，整体思路，面向对象设计原则的重要性。
      ·仍然改变不了过程式设计的通病，对“对象”“个体""实例”理解还不够深刻，希望在今后其他课程中可以继续锻炼。
