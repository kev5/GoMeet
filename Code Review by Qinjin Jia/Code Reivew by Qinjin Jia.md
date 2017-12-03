# EC601_Code Review :mag:
## Brief Introduction :sunglasses:
  Hello Everyone! :trollface::trollface::trollface::trollface:
  
  This is the page for **EC601(Fall 2017) A1 Code Review Assignment** :loop:
  
  |The project reviewed| **[@github.com/kev5/Go-Meet](https://github.com/kev5/Go-Meet)**
  |--|--
  |**The Reviewer**| :boy: **Qinjin Jia** qjia@bu.edu   :point_right:[@github/qinjinjia](https://github.com/qinjinjia)
  |**Criteria Followed**| [MIT 6.005 fall 15 - Reading 4 Code Review](http://web.mit.edu/6.005/www/fa15/classes/04-code-review/) 
  ||[Code Review Checklist](http://www.evoketechnologies.com/blog/code-review-checklist-perform-effective-code-reviews/)

  :sun_with_face: Summary of the Code Review Criteria :link: [Code Review Criteria.pdf](https://github.com/qinjinjia/ec601_Code-Review/blob/master/Code%20Review%20Criteria.pdf) 
 
 :full_moon_with_face: Code Review auxiliary tool: [jslint](http://www.jslint.com)
 
## Overall
 :new_moon_with_face: This is an exciting project **[Go & Meet](https://github.com/kev5/Go-Meet)**. The project is essentially a **social/recreational application** which gives information of all the events  (public/private) happening in the local vicinity of the user on a real-time basis.
 
 :new_moon: The Github Repository of the Project **[Go & Meet](https://github.com/kev5/Go-Meet)** is abundant and **there is a ReadMe file in the repository, which is perfect**.
 
 :waxing_crescent_moon: However, there are many files in the Repository and **GitHub file structure and directory structure is not clearly understandable**. It seems that it is because the project is still under development :construction:. 

 :first_quarter_moon: There are ten java files I reviewed:
          
 |File Name |Link |
 |--|--
 |ChooseLoginRegistrationActivity.java|:link: [ChooseLoginRegistrationActivity.java](https://github.com/kev5/Go-Meet/blob/master/ChooseLoginRegistrationActivity.java)|
 |Community.java|:link: [Community.java](https://github.com/kev5/Go-Meet/blob/master/Community.java)|
 |Database.java|:link: [Database.java](https://github.com/kev5/Go-Meet/blob/master/Database.java)|
 |LoginActivity.java|:link: [LoginActivity.java](https://github.com/kev5/Go-Meet/blob/master/LoginActivity.java)|
 |MainActivity.java|:link: [MainActivity.java](https://github.com/kev5/Go-Meet/blob/master/MainActivity.java)|
 |Post.java|:link: [Post.java](https://github.com/kev5/Go-Meet/blob/master/Post.java)|
 |RegistrationActivity.java|:link: [RegistrationActivity.java](https://github.com/kev5/Go-Meet/blob/master/RegistrationActivity.java)|
 |User.java|:link: [User.java](https://github.com/kev5/Go-Meet/blob/master/User.java)|
 |readFromFireBase.java|:link: [readFromFireBase.java](https://github.com/kev5/Go-Meet/blob/master/readFromFireBase.java)|
 |writeToFireBase.java|:link: [writeToFireBase.java](https://github.com/kev5/Go-Meet/blob/master/writeToFireBase.java)|
          
## Code Review

### 1. Code Formatiing

#### 1.1 Alignments
The uses of alignments are perfect. The code block starting point and ending point are easily identifiable.

#### 1.2 Naming Conventions
The **‘camelCode’** nameing convention is used in the project. The developers utilize capital letters to indicate the start of a word, which makes name of variables be **meaningful**.

#### 1.3 Code Layout
:thumbsup:Perfect! The code can fit in the standard 14-inch laptop screen.

#### 1.4 Commented Code
It seems that it is because the project is still under development :construction:. There are some commented code blocks in the project.

For instance, the commented code in [Database.java](https://github.com/kev5/Go-Meet/blob/master/Database.java) should be delected (Or removed from the master branch).   
```
public class Database {
    public FirebaseDatabase mDatabase;
    public DatabaseReference myRef;
//    public String ID = "690";                <-This commented code should be removed
    public Database(){
    }
```
and the commented code in [MainActivity.java](https://github.com/kev5/Go-Meet/blob/master/MainActivity.java) should be removed.
```
        al.add(post01.getPostText());
//        al.add(post02.getPostText());        <-This commented code should be removed
//        al.add(post03.getPostText());
//        al.add(post04.getPostText());
//        al.add(post05.getPostText());
//        al.add(post06.getPostText());
```

</br>

### 2. Architecture

It seems that the developers follow the [Singleton pattern](https://en.wikipedia.org/wiki/Singleton_pattern), which is good. 

Additionally, code is in sync with existing code patterns/technologies.

Therefore, the architecture of the project is nice.

</br>

### 3. Coding best practices

#### 3.1 Hard Coding
:thumbsup: Good!. There is no [hard coding](https://en.wikipedia.org/wiki/Hard_coding) in the project.

#### 3.2 Enumeration
The java file :link: [MainActivity.java](https://github.com/kev5/Go-Meet/blob/master/MainActivity.java) contains **[magic number](https://en.wikipedia.org/wiki/Magic_number_(programming))**. It is difficult to understand what is the meaning of 1, 2, 3 etc here.
This might be solved by utilizing the enumeration.
```
        mWrite.writePosts("1",post01);
        mWrite.writePosts("2",post02);
        mWrite.writePosts("3",post03);
        mWrite.writePosts("4",post04);
        mWrite.writePosts("5",post05);
        mWrite.writePosts("6",post06);
```
#### 3.3 Comments
There are some to-do comments mention pending tasks, which is good. For instance, in [User.java](https://github.com/kev5/Go-Meet/blob/master/User.java)
```
    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
```

However, the developers could add some comments to help others understand the code.

#### 3.4 Mul if/else blocks
:thumbsup: Good, there is no multiple if/else block in the project.

#### 3.5 Framework features
N/A

</br>

### 4. Non Functional requirements
#### 4.1 Maintainability(Supportability) 
The **Readability** of the code is good. The team uses **‘camelCode’ nameing convention**, which makes name of variables be meaningful.
The project is still under development :construction:, therefore, **"Testability", "Debuggability", "Configurability"** of the project are not reviewed.


#### 4.2 Reusability
Some places in the code violate [DRY (Do not Repeat Yourself) principle](https://en.wikipedia.org/wiki/Don%27t_repeat_yourself).Duplicated code is a risk to safety. If you have identical or very similar code in two places, then the fundamental risk is that there’s a bug in both copies, and some maintainer fixes the bug in one place but not the other. For instance, **DRY code** in :link: [writeToFireBase.java](https://github.com/kev5/Go-Meet/blob/master/writeToFireBase.java)
```
    public void likePost(String postID){
        DatabaseReference commentPostRef = this.writepostRef.child(postID);
        commentPostRef.child("likes").push().setValue(this.user.getUid());
    }

    public void dislikePost(String postID){
        DatabaseReference commentPostRef = this.writepostRef.child(postID);
        commentPostRef.child("dislikes").push().setValue(this.user.getUid());
    }
```

However, the project utilizes **generic functions and classes**, which increase reusability of the project. 

#### 4.3 Reliability:
Exception handling is not found in the code, which could be considered to add after achieveing the main functions of the software.

#### 4.4 Extensibility
The project utilizes classes and functions, which makes the code has **Good Extensibility**. The enhancements could be added to the existing code with minimal changes under this design(i.e. classes and functions).

#### 4.5 Security
The developers considered the security of the software, **Authentication is addded by utilizing the firebase** in [LoginActivity.java](https://github.com/kev5/Go-Meet/blob/master/LoginActivity.java).
```
        public void onClick(View v) {
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
```               

#### 4.6 Performance
The project is still under development :construction:, therefore, **Performance** of the project is not reviewed.

#### 4.7 Scalability
The **firebase realtime database** is utilized as the backend of the porject, which can be found in [writeToFireBase.java](https://github.com/kev5/Go-Meet/blob/master/writeToFireBase.java).  **The firebase realtime database** supports a large user base/data. Therefore, the scalability of the project is good.
```
    public writeToFireBase(){
        this.user = FirebaseAuth.getInstance().getCurrentUser();
        this.database = FirebaseDatabase.getInstance();
        this.writepostRef = this.database.getReference("posts");
        this.userRef = this.database.getReference("users");
    }
```

#### 4.8 Usability
The project is still under development :construction:, therefore, **Usability** of the project is not reviewed.

</br>

### 5. Object-Oriented Analysis and Design (OOAD) Principles

#### 5.1 Single Responsibility Principle(SRS)
The classes in the code are checked, each single class or function has one responsibility. Therefore, **the code obeys the Single Responsibility Principle (SRS)**. Nice design! :clap:

#### 5.2 Open Closed Principle
Owing to the developers' good design, each single class or function has one responsibility. While adding new functionality, existing code does not need to be modified. Therefore, the project obeys the **Open Closed Principle**.

#### 5.3 Liskov substitutability principle 
The class hierarchy does not exist in the project. Therefore, the project does not violate the **[Liskov substitutability principle]**(https://en.wikipedia.org/wiki/Liskov_substitution_principle)

#### 5.4 Interface segregation:
The project is still under development :construction:, therefore, **Interface segregation** of the project is not reviewed.

#### 5.5 Dependency Injection:
The project utilizes **firebase** to achieve authentication. Dependency injection is not applicable to the project.

## Summary
### Good Points: 
:full_moon_with_face: The overall code quality is high, the design of the software is good. 

:new_moon_with_face: The project has **proper code formatiing**，using **‘camelCode’ nameing convention** makes name of variables be **meaningful**.

:new_moon: The architecture of the project is nice.

:waxing_crescent_moon: There are some **to-do comments** mention pending tasks, which is good.

:first_quarter_moon: The **Readability** of the code is good. The developers considered the security of the software, **Authentication is addded by utilizing the firebase**.

:waxing_gibbous_moon: The project obeys **Single Responsibility Principle(SRS)**, **Open Closed Principle** and **Liskov substitutability principle**. 

### Can be better:
:full_moon: **GitHub file structure and directory structure is not clearly understandable**. It seems that it is because the project is still under development :construction:.

:waning_gibbous_moon:There are some **commented code blocks** in the project. The commented code blocks could keep in other branch rather than the master branch.

:last_quarter_moon: The developers could add **some comments to help others understand the code**.

:waning_crescent_moon: The java file :link: [MainActivity.java](https://github.com/kev5/Go-Meet/blob/master/MainActivity.java) contains **[magic number](https://en.wikipedia.org/wiki/Magic_number_(programming))**.


:sun_with_face: **Finally, Good Luck for your project :D)**:exclamation:

:trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface::trollface:
