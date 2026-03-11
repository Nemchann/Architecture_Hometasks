using System.Windows;

namespace MVVM
{
    public partial class PhoneWindow : Window
    {public PhoneWindow()
        {InitializeComponent();}
        private void Accept_Click(object sender, RoutedEventArgs e)
        { this.DialogResult = true;}
    }
}

//Это нужно для того чтобы новое окно закрывалось, тоже новый файл