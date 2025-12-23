public class CoreCourse extends Course { // بيورث من Course بياخد خصائص ودوال Course
    public CoreCourse(String name) {
        super(new Builder().setName(name).setType("Core"));
    }
} // → بيستقبل اسم الكورس كـ parameter
  // → بينادي الكونستركتور بتاع الأب Course وبيمررله Builder
  // → الـ Builder بيتم انشاؤه وبيتم تعيين الاسم والنوع "Core" للكورس
  // → إنشاء Object جديد من الكلاس Builder