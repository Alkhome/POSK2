import tkinter as tk
import tkinter.font as tkFont

START_BIT = "0"
STOP_BITS = "11"


class App:

    def __init__(self, root):
        s = "ąężćó ale padaka merde test".strip()
        #setting title
        root.title("undefined")
        #setting window size
        width = 700
        height = 400
        screenwidth = root.winfo_screenwidth()
        screenheight = root.winfo_screenheight()
        alignstr = '%dx%d+%d+%d' % (width, height, (screenwidth - width) / 2, (screenheight - height) / 2)
        root.geometry(alignstr)
        root.resizable(width=False, height=False)

        arr = []

        self.send_button = tk.Button(root)
        self.send_button["bg"] = "#ffffff"
        ft = tkFont.Font(family='Times', size=10)
        self.send_button["font"] = ft
        self.send_button["fg"] = "#000000"
        self.send_button["justify"] = "center"
        self.send_button["text"] = "Send"
        self.send_button.place(x=15, y=130, width=155, height=30)
        self.send_button["command"] = self.to_bit_conversion

        self.input_entry=tk.Entry(root, borderwidth=2, relief="ridge")
        self.input_entry["bg"] = "#ffffff"
        ft = tkFont.Font(family='Times', size=10)
        self.input_entry["font"] = ft
        self.input_entry["fg"] = "#333333"
        self.input_entry["justify"] = "center"
        self.input_entry["text"] = "input"
        self.input_entry.place(x=10, y=20, width=325, height=100)

        self.clear1_button = tk.Button(root)
        self.clear1_button["bg"] = "#ffffff"
        ft = tkFont.Font(family='Times', size=10)
        self.clear1_button["font"] = ft
        self.clear1_button["fg"] = "#000000"
        self.clear1_button["justify"] = "center"
        self.clear1_button["text"] = "Clear"
        self.clear1_button.place(x=175, y=130, width=155, height=30)
        self.clear1_button["command"] = self.clear1_button_command

        self.receive_button = tk.Button(root)
        self.receive_button["bg"] = "#ffffff"
        ft = tkFont.Font(family='Times', size=10)
        self.receive_button["font"] = ft
        self.receive_button["fg"] = "#000000"
        self.receive_button["justify"] = "center"
        self.receive_button["text"] = "Receive"
        self.receive_button.place(x=370, y=130, width=155, height=30)
        self.receive_button["command"] = self.to_text_conversion

        self.clear2_button = tk.Button(root)
        self.clear2_button["bg"] = "#ffffff"
        ft = tkFont.Font(family='Times', size=10)
        self.clear2_button["font"] = ft
        self.clear2_button["fg"] = "#000000"
        self.clear2_button["justify"] = "center"
        self.clear2_button["text"] = "Clear"
        self.clear2_button.place(x=530, y=130, width=155, height=30)
        self.clear2_button["command"] = self.clear2_button_command

        self.b_label = tk.Label(root, borderwidth=2, relief="ridge")
        self.b_label["bg"] = "#ffffff"
        self.b_label["wraplength"] = 650
        ft = tkFont.Font(family='Times', size=10)
        self.b_label["font"] = ft
        self.b_label["fg"] = "#333333"
        self.b_label["justify"] = "center"
        self.b_label["text"] = "BYTE STREAM"
        self.b_label.place(x=10, y=180, width=680, height=200)

        self.ascii_output_label = tk.Label(root, borderwidth=2, relief="ridge")
        self.ascii_output_label["bg"] = "#ffffff"
        self.ascii_output_label["wraplength"] = 300
        ft = tkFont.Font(family='Times', size=10)
        self.ascii_output_label["font"] = ft
        self.ascii_output_label["fg"] = "#333333"
        self.ascii_output_label["justify"] = "center"
        self.ascii_output_label["text"] = "RECEIVED BYTE STREAM"
        self.ascii_output_label.place(x=365, y=20, width=325, height=100)

    def clear1_button_command(self):
        print(str(self.input_entry.get()))
        self.input_entry.delete(0, "end")
        self.input_entry.insert(0, "")
        self.b_label.config(text="BYTE STREAM")

    def receive_button_command(self):
        print(str(self.b_label.cget()))

    def clear2_button_command(self):
        self.ascii_output_label.config(text="RECEIVED BYTE STREAM")

    def to_bit_conversion(self):
        s = str(self.input_entry.get())
        s = s.strip()
        arr = []
        for char in s:
            x = str(format(ord(char), 'b').zfill(8)) #zawsze 1 bajt
            arr.append(START_BIT + x[::-1] + STOP_BITS) #nie ma bitu 'parity'
        self.b_label.config(text=arr)
        return arr


    def to_text_conversion(self):
        arr = self.b_label.cget('text').split()
        text_file = open('przeklenstwa.txt', 'r')
        swears = []
        for line in text_file:
            swears.append(line.rstrip("\n"))

        s = ""
        for b in arr: #zamienia na znaki i usuwa bity start stop
            b = b[1:-2]
            s += chr(int(b[::-1], 2))

        for word in swears: #sprawdza czy nie ma brzydkich slow
            if word in s:
                i = s.index(word)
                length = len(word)
                s = s[:i] + "*"*length + s[i+length:]
        self.ascii_output_label.config(text=s)


if __name__ == '__main__':
    root = tk.Tk()
    app = App(root)
    root.mainloop()

