qustion_word_list = ['what', 'where', 'how', 'who', 'did', 'are', 'can', 'could', 'does', 'do', 'think', 'ask', 'was', 'is', 'why', 'will', 'wana', 'this']

def check_question(string);
    string_w = string.split(" ")
    return string_w[0].lower() in qustion_word_list
